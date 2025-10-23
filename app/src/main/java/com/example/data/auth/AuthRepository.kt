package com.example.data.auth

import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.net.ProtocolException
import java.util.concurrent.TimeUnit

class AuthRepository : AuthDataSource {
    private val api: ApiService
    private var authApi: ApiService? = null

    // Existing constructor that builds a simple client (kept for tests)
    constructor(baseUrl: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ApiService::class.java)
    }

    // New constructor that uses ApiClient (with interceptor/authenticator)
    constructor(context: Context, baseUrl: String) {
        api = ApiClient.create(context, baseUrl)
        // authApi será un cliente simple SIN authenticator para login/refresh
        authApi = buildAuthApi(baseUrl)
    }

    // Build a lightweight ApiService without authenticator for auth endpoints
    private fun buildAuthApi(baseUrl: String): ApiService {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val gson: Gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(ApiService::class.java)
    }

    override suspend fun login(usernameOrEmail: String, password: String, device: String?): LoginResponse? {
        val body = mutableMapOf<String, String>()
        body["usernameOrEmail"] = usernameOrEmail
        body["username_or_email"] = usernameOrEmail
        body["password"] = password
        if (!device.isNullOrEmpty()) body["device"] = device

        Log.d("AuthRepository", "login request: $body")

        try {
            val usedApi = authApi ?: api
            val resp = usedApi.loginMap(body)

            if (resp.isSuccessful) return resp.body()

            val code = resp.code()
            val err = resp.errorBody()?.string()
            Log.d("AuthRepository", "login failed code=$code body=$err")

            when (code) {
                401 -> throw Exception("Credenciales incorrectas. Revisa usuario/contraseña.")
                429 -> throw Exception("Demasiados intentos. Intenta de nuevo más tarde.")
                else -> throw Exception("Login fallido: $code ${err ?: "sin cuerpo"}")
            }
        } catch (t: Throwable) {
            Log.e("AuthRepository", "login exception", t)
            when (t) {
                is ProtocolException -> throw Exception("Error de red: demasiadas redirecciones/seguimientos. Revisa el servidor o intenta más tarde.")
                is IOException -> throw Exception("Error de conexión: verifica tu red y el servidor.")
                else -> throw Exception("Error en el login: ${t.message}")
            }
        }
    }

    override suspend fun register(req: RegisterRequest): LoginResponse? {
        Log.d("AuthRepository", "register request: $req")
        // Loggear el JSON que enviaremos para facilitar debugging en Logcat
        try {
            val gsonForLog = GsonBuilder().create()
            val jsonReq = gsonForLog.toJson(req)
            Log.d("AuthRepository", "register jsonBody: $jsonReq")
        } catch (e: Exception) {
            Log.w("AuthRepository", "failed to serialize register request for logging", e)
        }
        try {
            val usedApi = authApi ?: api
            val resp = usedApi.register(req)
            if (resp.isSuccessful) return resp.body()

            val errBody = resp.errorBody()?.string()
            Log.d("AuthRepository", "register failed code=${resp.code()} body=$errBody")

            // Intentar parsear JSON de error común {"error":"..."} o {"message":"..."}
            val parsedMessage = try {
                val gson = GsonBuilder().create()
                val type = object : TypeToken<Map<String, Any?>>() {}.type
                val map: Map<String, Any?> = gson.fromJson(errBody ?: "{}", type)
                (map["error"] ?: map["message"] ?: map["errors"] ?: errBody)?.toString()
            } catch (_: Exception) {
                errBody
            }

            throw Exception("Register failed: ${resp.code()} ${parsedMessage ?: "sin cuerpo"}")
        } catch (t: Throwable) {
            Log.e("AuthRepository", "register exception", t)
            when (t) {
                is IOException -> throw Exception("Error de conexión: verifica tu red y el servidor.")
                else -> throw Exception("Error en el registro: ${t.message}")
            }
        }
    }

    override suspend fun refresh(refreshToken: String): String? {
        val usedApi = authApi ?: api
        val resp = usedApi.refresh(mapOf("refreshToken" to refreshToken, "refresh_token" to refreshToken))
        if (resp.isSuccessful) return resp.body()?.accessTokenNormalized
        return null
    }

    override suspend fun logout(refreshToken: String) {
        api.logout(mapOf("refreshToken" to refreshToken, "refresh_token" to refreshToken))
    }

    override suspend fun revokeAll() {
        api.revokeAll()
    }

    override suspend fun me(): Map<String, Any?>? {
        try {
            val resp = api.me()
            if (resp.isSuccessful) return resp.body()
            return null
        } catch (t: Throwable) {
            Log.e("AuthRepository", "me() failed", t)
            return null
        }
    }

    override suspend fun oauthGoogle(idToken: String, inviteCode: String?, device: String?): LoginResponse? {
        val usedApi = authApi ?: api
        val body = mutableMapOf<String, String>("idToken" to idToken)
        if (!inviteCode.isNullOrEmpty()) body["inviteCode"] = inviteCode
        if (!device.isNullOrEmpty()) body["device"] = device
        Log.d("AuthRepository", "oauthGoogle request: keys=${body.keys}")
        val resp = usedApi.oauthGoogle(body)
        if (resp.isSuccessful) return resp.body()
        val code = resp.code()
        val err = resp.errorBody()?.string()
        throw Exception("Google OAuth falló: $code ${err ?: "sin cuerpo"}")
    }

    override suspend fun oauthFacebook(accessToken: String, inviteCode: String?, device: String?): LoginResponse? {
        val usedApi = authApi ?: api
        val body = mutableMapOf<String, String>("accessToken" to accessToken)
        if (!inviteCode.isNullOrEmpty()) body["inviteCode"] = inviteCode
        if (!device.isNullOrEmpty()) body["device"] = device
        Log.d("AuthRepository", "oauthFacebook request: keys=${body.keys}")
        val resp = usedApi.oauthFacebook(body)
        if (resp.isSuccessful) return resp.body()
        val code = resp.code()
        val err = resp.errorBody()?.string()
        throw Exception("Facebook OAuth falló: $code ${err ?: "sin cuerpo"}")
    }
}
