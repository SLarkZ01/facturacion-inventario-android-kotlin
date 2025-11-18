# CategoriasApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**actualizarCategoria**](CategoriasApi.md#actualizarCategoria) | **PUT** /api/categorias/{id} | Actualizar categoría |
| [**crearCategoria**](CategoriasApi.md#crearCategoria) | **POST** /api/categorias | Crear categoría |
| [**eliminar1**](CategoriasApi.md#eliminar1) | **DELETE** /api/categorias/{id} | Eliminar categoría |
| [**getCategoria1**](CategoriasApi.md#getCategoria1) | **GET** /api/categorias/{id} | Obtener categoría por ID |
| [**listar3**](CategoriasApi.md#listar3) | **GET** /api/categorias | Buscar/listar categorías |


<a id="actualizarCategoria"></a>
# **actualizarCategoria**
> actualizarCategoria(id, categoriaRequest)

Actualizar categoría

Actualiza los datos de una categoría. Envía solo los campos que deseas actualizar.  **IMPORTANTE sobre &#x60;listaMedios&#x60;:** - Si actualizas &#x60;listaMedios&#x60;, DEBE incluir &#x60;publicId&#x60; en cada medio - Al actualizar, se reemplazan las imágenes anteriores (las viejas se eliminan de Cloudinary automáticamente) - Para agregar nuevas imágenes manteniendo las existentes, primero obtén la categoría actual y agrega a su array  **Campos actualizables:** - &#x60;nombre&#x60;: Nombre de la categoría - &#x60;descripcion&#x60;: Descripción - &#x60;tallerId&#x60;: ID del taller (normalmente no se cambia) - &#x60;listaMedios&#x60;: Array de medios (DEBE incluir publicId) - &#x60;mappedGlobalCategoryId&#x60;: ID de categoría global 

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CategoriasApi()
val id : kotlin.String = 507f1f77bcf86cd799439011 // kotlin.String | ID de la categoría
val categoriaRequest : CategoriaRequest = {"nombre":"Filtros Premium","descripcion":"Filtros de alta calidad para motos"} // CategoriaRequest | Datos actualizados de la categoría (solo enviar los campos a modificar). Si actualizas `listaMedios`, DEBE incluir `publicId` en cada elemento. 
try {
    apiInstance.actualizarCategoria(id, categoriaRequest)
} catch (e: ClientException) {
    println("4xx response calling CategoriasApi#actualizarCategoria")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CategoriasApi#actualizarCategoria")
    e.printStackTrace()
}
```

### Parameters
| **id** | **kotlin.String**| ID de la categoría | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **categoriaRequest** | [**CategoriaRequest**](CategoriaRequest.md)| Datos actualizados de la categoría (solo enviar los campos a modificar). Si actualizas &#x60;listaMedios&#x60;, DEBE incluir &#x60;publicId&#x60; en cada elemento.  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="crearCategoria"></a>
# **crearCategoria**
> crearCategoria(categoriaRequest)

Crear categoría

Crea una nueva categoría de productos.  **IMPORTANTE:** - &#x60;tallerId&#x60; es REQUERIDO para crear categorías (validación en el servicio) - Si no se proporciona &#x60;tallerId&#x60;, el backend devolverá: {\&quot;error\&quot;: \&quot;tallerId es obligatorio para crear una categoría\&quot;}  **Gestión de Imágenes:** - Las imágenes DEBEN subirse primero a Cloudinary usando &#x60;/api/uploads/cloudinary-sign&#x60; - Cada objeto en &#x60;listaMedios&#x60; DEBE incluir el campo &#x60;publicId&#x60; (CRÍTICO para eliminar imágenes al borrar la categoría) - Sin &#x60;publicId&#x60;, las imágenes quedarán huérfanas en Cloudinary y no se podrán eliminar automáticamente  **Estructura de &#x60;listaMedios&#x60;:** - Cada elemento debe tener: {type, publicId, secure_url, format, width, height, order} - El campo &#x60;publicId&#x60; es retornado por Cloudinary como &#x60;public_id&#x60; al subir la imagen  **Flujo recomendado para imágenes:** 1. Obtener firma: POST /api/uploads/cloudinary-sign con {folder: \&quot;products\&quot;} 2. Subir a Cloudinary con la firma obtenida 3. Guardar en &#x60;listaMedios&#x60; el objeto completo que devuelve Cloudinary (especialmente &#x60;public_id&#x60;) 

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CategoriasApi()
val categoriaRequest : CategoriaRequest = {"nombre":"Filtros","descripcion":"Filtros de aceite, aire y combustible","tallerId":"507f1f77bcf86cd799439777","listaMedios":[{"type":"image/jpeg","publicId":"products/507f1f77/categoria-filtros-abc123","secure_url":"https://res.cloudinary.com/df7ggzasi/image/upload/v1763282466/products/507f1f77/categoria-filtros-abc123.jpg","url":"https://res.cloudinary.com/df7ggzasi/image/upload/v1763282466/products/507f1f77/categoria-filtros-abc123.jpg","format":"jpg","width":800,"height":600,"order":0}],"mappedGlobalCategoryId":"global-cat-filtros"} // CategoriaRequest | Datos de la categoría. **REQUERIDO**: `nombre` y `tallerId` (el backend validará que `tallerId` esté presente) **CRÍTICO**: Si incluyes `listaMedios`, cada medio DEBE tener `publicId` para poder eliminar las imágenes de Cloudinary. 
try {
    apiInstance.crearCategoria(categoriaRequest)
} catch (e: ClientException) {
    println("4xx response calling CategoriasApi#crearCategoria")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CategoriasApi#crearCategoria")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **categoriaRequest** | [**CategoriaRequest**](CategoriaRequest.md)| Datos de la categoría. **REQUERIDO**: &#x60;nombre&#x60; y &#x60;tallerId&#x60; (el backend validará que &#x60;tallerId&#x60; esté presente) **CRÍTICO**: Si incluyes &#x60;listaMedios&#x60;, cada medio DEBE tener &#x60;publicId&#x60; para poder eliminar las imágenes de Cloudinary.  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="eliminar1"></a>
# **eliminar1**
> eliminar1(id)

Eliminar categoría

Elimina una categoría por ID.  **IMPORTANTE:** - También elimina automáticamente las imágenes asociadas de Cloudinary (si existen en &#x60;listaMedios&#x60; con &#x60;publicId&#x60;) - Si la categoría tiene productos asociados, puede fallar (dependiendo de la lógica de negocio) - Esta acción no se puede deshacer  **Limpieza automática:** - Todas las imágenes en &#x60;listaMedios&#x60; que tengan &#x60;publicId&#x60; serán eliminadas de Cloudinary - Si alguna imagen no tiene &#x60;publicId&#x60;, quedará huérfana en Cloudinary 

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CategoriasApi()
val id : kotlin.String = 507f1f77bcf86cd799439011 // kotlin.String | ID de la categoría a eliminar
try {
    apiInstance.eliminar1(id)
} catch (e: ClientException) {
    println("4xx response calling CategoriasApi#eliminar1")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CategoriasApi#eliminar1")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.String**| ID de la categoría a eliminar | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="getCategoria1"></a>
# **getCategoria1**
> getCategoria1(id)

Obtener categoría por ID

Devuelve los detalles completos de una categoría.  **La respuesta incluye:** - Datos básicos (nombre, descripción) - &#x60;tallerId&#x60;: ID del taller propietario - &#x60;listaMedios&#x60;: Array con imágenes (cada una con publicId, secure_url, etc.) - &#x60;mappedGlobalCategoryId&#x60;: ID de categoría global (si existe)  **Nota:** Este endpoint es público (no requiere autenticación) 

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CategoriasApi()
val id : kotlin.String = 507f1f77bcf86cd799439011 // kotlin.String | ID de la categoría
try {
    apiInstance.getCategoria1(id)
} catch (e: ClientException) {
    println("4xx response calling CategoriasApi#getCategoria1")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CategoriasApi#getCategoria1")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.String**| ID de la categoría | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="listar3"></a>
# **listar3**
> listar3(tallerId, q, page, size, todas)

Buscar/listar categorías

Busca categorías por nombre o lista categorías de un taller. Por defecto &#x60;tallerId&#x60; es obligatorio; usar &#x60;todas&#x3D;true&#x60; sólo si se es platform-admin para obtener todas las categorías.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CategoriasApi()
val tallerId : kotlin.String = 507f1f77bcf86cd799439777 // kotlin.String | ID del taller (obligatorio para listar)
val q : kotlin.String = filtro // kotlin.String | Término de búsqueda para nombre de categoría
val page : kotlin.Int = 0 // kotlin.Int | Número de página
val size : kotlin.Int = 20 // kotlin.Int | Elementos por página
val todas : kotlin.Boolean = false // kotlin.Boolean | Si true y el caller es platform-admin devuelve todas las categorías
try {
    apiInstance.listar3(tallerId, q, page, size, todas)
} catch (e: ClientException) {
    println("4xx response calling CategoriasApi#listar3")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CategoriasApi#listar3")
    e.printStackTrace()
}
```

### Parameters
| **tallerId** | **kotlin.String**| ID del taller (obligatorio para listar) | |
| **q** | **kotlin.String**| Término de búsqueda para nombre de categoría | [optional] |
| **page** | **kotlin.Int**| Número de página | [optional] [default to 0] |
| **size** | **kotlin.Int**| Elementos por página | [optional] [default to 20] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **todas** | **kotlin.Boolean**| Si true y el caller es platform-admin devuelve todas las categorías | [optional] [default to false] |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

