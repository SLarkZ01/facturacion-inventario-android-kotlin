# CategoriasApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**actualizarCategoria**](CategoriasApi.md#actualizarCategoria) | **PUT** /api/categorias/{id} | Actualizar categoría |
| [**crearCategoria**](CategoriasApi.md#crearCategoria) | **POST** /api/categorias | Crear categoría |
| [**eliminar1**](CategoriasApi.md#eliminar1) | **DELETE** /api/categorias/{id} | Eliminar categoría |
| [**getCategoria**](CategoriasApi.md#getCategoria) | **GET** /api/categorias/{id} | Obtener categoría |
| [**listar2**](CategoriasApi.md#listar2) | **GET** /api/categorias | Buscar/listar categorías |


<a id="actualizarCategoria"></a>
# **actualizarCategoria**
> actualizarCategoria(id, categoriaRequest)

Actualizar categoría

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CategoriasApi()
val id : kotlin.String = id_example // kotlin.String | 
val categoriaRequest : CategoriaRequest =  // CategoriaRequest | 
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
| **id** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **categoriaRequest** | [**CategoriaRequest**](CategoriaRequest.md)|  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="crearCategoria"></a>
# **crearCategoria**
> crearCategoria(categoriaRequest)

Crear categoría

Crea una nueva categoría de productos. Nota: &#x60;tallerId&#x60; es obligatorio ya que todas las categorías pertenecen a un taller.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CategoriasApi()
val categoriaRequest : CategoriaRequest = {"nombre":"Filtros","descripcion":"Filtros de aceite, aire y combustible","iconoRecurso":2131230988,"tallerId":"507f1f77bcf86cd799439777","listaMedios":[{"type":"image","publicId":"products/507f1f77/abc123","secure_url":"https://res.cloudinary.com/df7ggzasi/image/upload/v1/products/abc123.jpg","format":"jpg","order":0}]} // CategoriaRequest | Datos de la categoría (tallerId obligatorio). `listaMedios` acepta una lista de objetos con campos: type, publicId, secure_url, format, order.
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
| **categoriaRequest** | [**CategoriaRequest**](CategoriaRequest.md)| Datos de la categoría (tallerId obligatorio). &#x60;listaMedios&#x60; acepta una lista de objetos con campos: type, publicId, secure_url, format, order. | |

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

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CategoriasApi()
val id : kotlin.String = id_example // kotlin.String | 
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
| **id** | **kotlin.String**|  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a id="getCategoria"></a>
# **getCategoria**
> getCategoria(id)

Obtener categoría

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CategoriasApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    apiInstance.getCategoria(id)
} catch (e: ClientException) {
    println("4xx response calling CategoriasApi#getCategoria")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CategoriasApi#getCategoria")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.String**|  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a id="listar2"></a>
# **listar2**
> listar2(tallerId, q, page, size, todas)

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
    apiInstance.listar2(tallerId, q, page, size, todas)
} catch (e: ClientException) {
    println("4xx response calling CategoriasApi#listar2")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CategoriasApi#listar2")
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

