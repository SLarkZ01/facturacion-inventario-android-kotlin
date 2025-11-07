# FavoritosApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**add**](FavoritosApi.md#add) | **POST** /api/favoritos/productos/{productoId} | Agregar producto a favoritos |
| [**isFav**](FavoritosApi.md#isFav) | **GET** /api/favoritos/productos/{productoId}/es-favorito | Comprobar favorito |
| [**list**](FavoritosApi.md#list) | **GET** /api/favoritos | Listar favoritos |
| [**remove1**](FavoritosApi.md#remove1) | **DELETE** /api/favoritos/productos/{productoId} | Remover producto de favoritos |


<a id="add"></a>
# **add**
> add(productoId)

Agregar producto a favoritos

Añade un producto a la lista de favoritos del usuario autenticado

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FavoritosApi()
val productoId : kotlin.String = 507f191e810c19729de860ea // kotlin.String | ID del producto a agregar
try {
    apiInstance.add(productoId)
} catch (e: ClientException) {
    println("4xx response calling FavoritosApi#add")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FavoritosApi#add")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **productoId** | **kotlin.String**| ID del producto a agregar | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="isFav"></a>
# **isFav**
> isFav(productoId)

Comprobar favorito

Comprueba si un producto está en la lista de favoritos del usuario autenticado

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FavoritosApi()
val productoId : kotlin.String = 507f191e810c19729de860ea // kotlin.String | ID del producto a verificar
try {
    apiInstance.isFav(productoId)
} catch (e: ClientException) {
    println("4xx response calling FavoritosApi#isFav")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FavoritosApi#isFav")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **productoId** | **kotlin.String**| ID del producto a verificar | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="list"></a>
# **list**
> list(page, size)

Listar favoritos

Devuelve una lista paginada de productos favoritos del usuario autenticado

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FavoritosApi()
val page : kotlin.Int = 0 // kotlin.Int | Número de página (base 0)
val size : kotlin.Int = 20 // kotlin.Int | Elementos por página
try {
    apiInstance.list(page, size)
} catch (e: ClientException) {
    println("4xx response calling FavoritosApi#list")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FavoritosApi#list")
    e.printStackTrace()
}
```

### Parameters
| **page** | **kotlin.Int**| Número de página (base 0) | [optional] [default to 0] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **size** | **kotlin.Int**| Elementos por página | [optional] [default to 20] |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="remove1"></a>
# **remove1**
> remove1(productoId)

Remover producto de favoritos

Remueve un producto de la lista de favoritos del usuario autenticado

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FavoritosApi()
val productoId : kotlin.String = 507f191e810c19729de860ea // kotlin.String | ID del producto a remover
try {
    apiInstance.remove1(productoId)
} catch (e: ClientException) {
    println("4xx response calling FavoritosApi#remove1")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FavoritosApi#remove1")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **productoId** | **kotlin.String**| ID del producto a remover | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

