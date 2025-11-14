package com.example.facturacion_inventario.data.repository

import android.util.Log
import com.example.facturacion_inventario.data.remote.api.RetrofitClient
import com.example.facturacion_inventario.data.remote.mapper.CategoriaMapper
import com.example.facturacion_inventario.domain.model.Category
import com.example.facturacion_inventario.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementación del repositorio que consume la API real de Spring Boot
 * para categorías del backend
 */
class RemoteCategoryRepository : CategoryRepository {

    private val apiService = RetrofitClient.categoriaApiService
    private val TAG = "RemoteCategoryRepo"

    /**
     * Obtiene todas las categorías (sin filtros)
     * Nota: Este método es síncrono según la interfaz, pero idealmente debería ser suspend
     * Por ahora retornamos lista vacía y usamos getCategoriesAsync
     */
    override fun getCategories(): List<Category> {
        Log.w(TAG, "getCategories() síncrono no recomendado, usar getCategoriesAsync()")
        return emptyList()
    }

    /**
     * Obtiene una categoría por ID
     */
    override fun getCategoryById(id: String): Category? {
        Log.w(TAG, "getCategoryById() síncrono no recomendado, usar getCategoryByIdAsync()")
        return null
    }

    /**
     * Obtiene todas las categorías de forma asíncrona
     * @param query Búsqueda opcional por nombre
     * @param tallerId Filtro opcional por taller
     * @param global Si true devuelve solo categorías globales
     * @param todas Si true devuelve TODAS las categorías (globales + talleres)
     * @param page Número de página (default: 0)
     * @param size Elementos por página (default: 20)
     *
     * IMPORTANTE:
     * - Por defecto (sin parámetros): Solo categorías GLOBALES (tallerId = null)
     * - Con todas=true: TODAS las categorías (globales + de talleres)
     * - Con tallerId="xxx": Solo categorías de ese taller
     */
    suspend fun getCategoriesAsync(
        query: String? = null,
        tallerId: String? = null,
        global: Boolean = false,
        todas: Boolean = false, // ← Parámetro para obtener todas
        page: Int = 0,
        size: Int = 20
    ): Result<List<Category>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "📡 API Call - Fetching categories...")
                Log.d(TAG, "  Parameters: query=$query, tallerId=$tallerId, global=$global, todas=$todas, page=$page, size=$size")

                val response = apiService.listarCategorias(
                    query = query,
                    page = page,
                    size = size,
                    tallerId = tallerId,
                    global = global,
                    todas = todas // ← Pasar el parámetro al backend
                )

                Log.d(TAG, "📡 Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d(TAG, "📦 Response body: $body")
                    val categorias = body?.categorias ?: emptyList()
                    Log.d(TAG, "✅ Successfully fetched ${categorias.size} categories from API")
                    categorias.forEachIndexed { index, cat ->
                        Log.d(TAG, "  API Category [$index]: ${cat.nombre} (id: ${cat.id})")
                    }
                    Result.success(CategoriaMapper.toDomainList(categorias))
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = "Error ${response.code()}: ${response.message()} - $errorBody"
                    Log.e(TAG, "❌ API Error: $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception fetching categories: ${e.javaClass.simpleName}", e)
                Log.e(TAG, "   Message: ${e.message}")
                e.printStackTrace()
                Result.failure(e)
            }
        }
    }

    /**
     * Obtiene una categoría por ID de forma asíncrona
     */
    suspend fun getCategoryByIdAsync(id: String): Result<Category> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Fetching category with id: $id")
                val response = apiService.obtenerCategoria(id)

                if (response.isSuccessful) {
                    val categoria = response.body()?.categoria
                    if (categoria != null) {
                        Log.d(TAG, "Successfully fetched category: ${categoria.nombre}")
                        Result.success(CategoriaMapper.toDomain(categoria))
                    } else {
                        val errorMsg = "Category not found"
                        Log.e(TAG, errorMsg)
                        Result.failure(Exception(errorMsg))
                    }
                } else {
                    val errorMsg = "Error ${response.code()}: ${response.message()}"
                    Log.e(TAG, errorMsg)
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception fetching category by id", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Busca categorías por nombre
     */
    suspend fun searchCategories(query: String, page: Int = 0, size: Int = 20): Result<List<Category>> {
        return getCategoriesAsync(query = query, page = page, size = size)
    }

    /**
     * Obtiene categorías globales (tallerId == null)
     * Comportamiento por defecto de la app pública
     */
    @Suppress("unused")
    suspend fun getGlobalCategories(page: Int = 0, size: Int = 20): Result<List<Category>> {
        return getCategoriesAsync(global = true, todas = false, page = page, size = size)
    }

    /**
     * Obtiene categorías por taller
     */
    @Suppress("unused")
    suspend fun getCategoriesByTaller(tallerId: String, page: Int = 0, size: Int = 20): Result<List<Category>> {
        return getCategoriesAsync(tallerId = tallerId, todas = false, page = page, size = size)
    }

    /**
     * Obtiene TODAS las categorías (globales + de talleres) sin filtros
     * Útil para administración o listados completos
     * USA EL PARÁMETRO todas=true DEL BACKEND
     */
    @Suppress("unused")
    suspend fun getAllCategoriesWithoutFilter(page: Int = 0, size: Int = 100): Result<List<Category>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "📡 Fetching ALL categories (globales + talleres)")

                val response = apiService.listarCategorias(
                    todas = true,  // ← Parámetro clave para obtener todas
                    page = page,
                    size = size
                )

                Log.d(TAG, "📡 Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val categorias = response.body()?.categorias ?: emptyList()
                    Log.d(TAG, "✅ Total categories fetched (sin filtro): ${categorias.size}")
                    categorias.forEachIndexed { index, cat ->
                        val tipo = if (cat.tallerId == null) "GLOBAL" else "TALLER(${cat.tallerId})"
                        Log.d(TAG, "  [$index] ${cat.nombre} - $tipo")
                    }
                    Result.success(CategoriaMapper.toDomainList(categorias))
                } else {
                    val errorMsg = "Error ${response.code()}: ${response.message()}"
                    Log.e(TAG, "❌ $errorMsg")
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Exception fetching all categories: ${e.message}", e)
                Result.failure(e)
            }
        }
    }
}
