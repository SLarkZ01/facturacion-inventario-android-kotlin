package com.example.auth

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.UUID
import java.util.concurrent.TimeUnit

object ApiClient {
    fun create(context: Context, baseUrl: String): ApiService {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        // Interceptor para trazar peticiones con un UUID y log en Logcat
        val traceInterceptor = Interceptor { chain ->
            val req = chain.request()
            val id = UUID.randomUUID().toString()
            val newReq = req.newBuilder()
                .addHeader("X-Request-Id", id)
                .addHeader("X-Request-From-App", "true")
                .build()

            Log.d("ApiClient-Request", "id=$id method=${newReq.method} url=${newReq.url}")

            val resp = chain.proceed(newReq)

            Log.d("ApiClient-Request", "id=$id response=${resp.code} for ${newReq.url}")
            resp
        }

        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            // No añadir Authorization para endpoints de auth (login/refresh/oauth)
            val path = req.url.encodedPath
            if (path.contains("/api/auth/login") || path.contains("/api/auth/refresh") || path.contains("/api/auth/oauth/")) {
                return@Interceptor chain.proceed(req)
            }

            val reqBuilder = req.newBuilder()
            val token = TokenStorage.getAccessToken(context)
            if (!token.isNullOrEmpty()) {
                reqBuilder.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(reqBuilder.build())
        }

        val authenticator = object : Authenticator {
            // Cuenta cuántas respuestas previas hubo para evitar reintentos infinitos
            private fun responseCount(response: Response?): Int {
                var res = response
                var result = 1
                while (res?.priorResponse != null) {
                    result++
                    res = res.priorResponse
                }
                return result
            }

            override fun authenticate(route: Route?, response: Response): Request? {
                Log.d("ApiClient-Auth", "authenticate called: code=${response.code} url=${response.request.url}")

                // Sólo intentamos refresh para 401 (no para 400/403/5xx)
                if (response.code != 401) {
                    Log.d("ApiClient-Auth", "not 401 -> no refresh")
                    return null
                }

                // Si no había un header Authorization en la petición original, no intentamos refresh
                if (response.request.header("Authorization") == null) {
                    Log.d("ApiClient-Auth", "original request had no Authorization header -> no refresh")
                    return null
                }

                // Evitar bucles: si ya intentamos al menos una vez, no reintentar
                val count = responseCount(response)
                Log.d("ApiClient-Auth", "prior response count=$count")
                if (count >= 2) {
                    Log.w("ApiClient", "authenticate: already attempted to authenticate, giving up")
                    return null
                }

                // No intentar refresh si la petición original era al endpoint de login, refresh u oauth
                val originalUrl = response.request.url.encodedPath
                if (originalUrl.contains("/api/auth/login") || originalUrl.contains("/api/auth/refresh") || originalUrl.contains("/api/auth/oauth/")) {
                    Log.w("ApiClient", "authenticate: not refreshing token for auth endpoints: $originalUrl")
                    return null
                }

                val refresh = TokenStorage.getRefreshToken(context)
                if (refresh.isNullOrEmpty()) {
                    Log.d("ApiClient-Auth", "no refresh token available -> clearing tokens and abort")
                    TokenStorage.clear(context)
                    return null
                }

                try {
                    // Build a simple OkHttpClient without authenticator to call refresh endpoint synchronously
                    val client = OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build()

                    val json = "{\"refreshToken\":\"${refresh}\"}"
                    val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())
                    val req = Request.Builder()
                        .url(baseUrl.trimEnd('/') + "/api/auth/refresh")
                        .post(body)
                        .build()

                    val resp = client.newCall(req).execute()

                    Log.d("ApiClient-Auth", "refresh request returned code=${resp.code}")

                    if (!resp.isSuccessful) {
                        Log.w("ApiClient-Auth", "refresh failed -> clearing tokens")
                        TokenStorage.clear(context)
                        return null
                    }

                    val gson: Gson = GsonBuilder().create()
                    val respBody = resp.body?.string() ?: run {
                        Log.w("ApiClient-Auth", "refresh response had empty body -> clearing tokens")
                        TokenStorage.clear(context)
                        return null
                    }

                    Log.d("ApiClient-Auth", "refresh response body=$respBody")

                    val refreshResp = gson.fromJson(respBody, RefreshResponse::class.java)
                    val newAccess = refreshResp?.accessTokenNormalized
                    if (newAccess.isNullOrEmpty()) {
                        Log.w("ApiClient-Auth", "refresh response missing access token -> clearing tokens")
                        TokenStorage.clear(context)
                        return null
                    }

                    TokenStorage.setAccessToken(context, newAccess)

                    // Retry original request with new token
                    Log.d("ApiClient-Auth", "refresh succeeded, retrying original request with new token")
                    return response.request.newBuilder()
                        .header("Authorization", "Bearer $newAccess")
                        .build()

                } catch (ex: IOException) {
                    Log.e("ApiClient", "refresh token failed", ex)
                    TokenStorage.clear(context)
                    return null
                }
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(traceInterceptor)
            .addInterceptor(logger)
            .addInterceptor(authInterceptor)
            .authenticator(authenticator)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            // Evitar redirecciones automáticas y reintentos que pueden crear bucles y multiplicar requests
            .followRedirects(false)
            .followSslRedirects(false)
            .retryOnConnectionFailure(false)
            .build()

        val gson: Gson = GsonBuilder().create()
        val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(ApiService::class.java)
    }
}