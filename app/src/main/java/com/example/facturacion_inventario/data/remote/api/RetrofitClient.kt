package com.example.facturacion_inventario.data.remote.api

import android.content.Context
import com.example.data.auth.ApiConfig
import com.example.data.auth.TokenStorage
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit singleton para la API de productos, categorías y carritos
 * Ahora incluye autenticación JWT para evitar errores 401
 */
object RetrofitClient {

    private var context: Context? = null

    /**
     * Inicializa el cliente con el contexto de la aplicación
     * Llamar desde Application o MainActivity
     */
    fun initialize(appContext: Context) {
        context = appContext.applicationContext
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Interceptor de autenticación que agrega el token JWT
     */
    private val authInterceptor = Interceptor { chain ->
        val req = chain.request()
        val path = req.url.encodedPath

        // No agregar token a endpoints públicos
        if (path.contains("/api/auth/")) {
            return@Interceptor chain.proceed(req)
        }

        val reqBuilder = req.newBuilder()
        context?.let { ctx ->
            val token = TokenStorage.getAccessToken(ctx)
            if (!token.isNullOrEmpty()) {
                reqBuilder.addHeader("Authorization", "Bearer $token")
            }
        }
        chain.proceed(reqBuilder.build())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        .setLenient()
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val productoApiService: ProductoApiService by lazy {
        retrofit.create(ProductoApiService::class.java)
    }

    val categoriaApiService: CategoriaApiService by lazy {
        retrofit.create(CategoriaApiService::class.java)
    }

    val carritoApiService: CarritoApiService by lazy {
        retrofit.create(CarritoApiService::class.java)
    }
}
