# CarritosApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**addItem**](CarritosApi.md#addItem) | **POST** /api/carritos/{id}/items | Agregar item a carrito |
| [**clear**](CarritosApi.md#clear) | **POST** /api/carritos/{id}/clear | Vaciar carrito |
| [**crear**](CarritosApi.md#crear) | **POST** /api/carritos | Crear carrito |
| [**delete1**](CarritosApi.md#delete1) | **DELETE** /api/carritos/{id} | Eliminar carrito |
| [**getById**](CarritosApi.md#getById) | **GET** /api/carritos/{id} | Obtener carrito por ID |
| [**listarPorUsuario1**](CarritosApi.md#listarPorUsuario1) | **GET** /api/carritos | Listar carritos por usuario |
| [**merge**](CarritosApi.md#merge) | **POST** /api/carritos/merge | Merge de carrito anónimo |
| [**removeItem**](CarritosApi.md#removeItem) | **DELETE** /api/carritos/{id}/items/{productoId} | Remover item de carrito |


<a id="addItem"></a>
# **addItem**
> addItem(id, carritoItemRequest)

Agregar item a carrito

Agrega un producto al carrito del usuario autenticado. Si el producto ya existe, actualiza la cantidad.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CarritosApi()
val id : kotlin.String = 507f1f77bcf86cd799439999 // kotlin.String | ID del carrito
val carritoItemRequest : CarritoItemRequest = {"productoId":"507f191e810c19729de860ea","cantidad":2,"precioUnitario":25.5} // CarritoItemRequest | Item a agregar
try {
    apiInstance.addItem(id, carritoItemRequest)
} catch (e: ClientException) {
    println("4xx response calling CarritosApi#addItem")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CarritosApi#addItem")
    e.printStackTrace()
}
```

### Parameters
| **id** | **kotlin.String**| ID del carrito | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **carritoItemRequest** | [**CarritoItemRequest**](CarritoItemRequest.md)| Item a agregar | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="clear"></a>
# **clear**
> clear(id)

Vaciar carrito

Elimina todos los items del carrito

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CarritosApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    apiInstance.clear(id)
} catch (e: ClientException) {
    println("4xx response calling CarritosApi#clear")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CarritosApi#clear")
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

<a id="crear"></a>
# **crear**
> crear(carritoRequest)

Crear carrito

Crea un carrito nuevo para un usuario o anónimo. Si se omite usuarioId, crea un carrito anónimo que puede sincronizarse después del login.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CarritosApi()
val carritoRequest : CarritoRequest = {"usuarioId":"507f1f77bcf86cd799439011","items":[{"productoId":"507f191e810c19729de860ea","cantidad":2,"precioUnitario":25.5}]} // CarritoRequest | Datos del carrito
try {
    apiInstance.crear(carritoRequest)
} catch (e: ClientException) {
    println("4xx response calling CarritosApi#crear")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CarritosApi#crear")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **carritoRequest** | [**CarritoRequest**](CarritoRequest.md)| Datos del carrito | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="delete1"></a>
# **delete1**
> delete1(id)

Eliminar carrito

Elimina un carrito por id

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CarritosApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    apiInstance.delete1(id)
} catch (e: ClientException) {
    println("4xx response calling CarritosApi#delete1")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CarritosApi#delete1")
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

<a id="getById"></a>
# **getById**
> getById(id)

Obtener carrito por ID

Devuelve un carrito por su id

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CarritosApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    apiInstance.getById(id)
} catch (e: ClientException) {
    println("4xx response calling CarritosApi#getById")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CarritosApi#getById")
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

<a id="listarPorUsuario1"></a>
# **listarPorUsuario1**
> listarPorUsuario1(usuarioId)

Listar carritos por usuario

Devuelve los carritos asociados a un usuario

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CarritosApi()
val usuarioId : kotlin.String = usuarioId_example // kotlin.String | 
try {
    apiInstance.listarPorUsuario1(usuarioId)
} catch (e: ClientException) {
    println("4xx response calling CarritosApi#listarPorUsuario1")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CarritosApi#listarPorUsuario1")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **usuarioId** | **kotlin.String**|  | [optional] |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a id="merge"></a>
# **merge**
> merge(requestBody)

Merge de carrito anónimo

Sincroniza un carrito anónimo al usuario autenticado tras el login. Combina items del carrito anónimo con el carrito del usuario.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CarritosApi()
val requestBody : kotlin.collections.Map<kotlin.String, kotlin.Any> = {"anonCartId":"507f1f77bcf86cd799439888","items":[{"productoId":"507f191e810c19729de860ea","cantidad":3,"precioUnitario":25.5}]} // kotlin.collections.Map<kotlin.String, kotlin.Any> | ID del carrito anónimo y/o items a sincronizar
try {
    apiInstance.merge(requestBody)
} catch (e: ClientException) {
    println("4xx response calling CarritosApi#merge")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CarritosApi#merge")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestBody** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md)| ID del carrito anónimo y/o items a sincronizar | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="removeItem"></a>
# **removeItem**
> removeItem(id, productoId)

Remover item de carrito

Remueve un item del carrito del usuario autenticado

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = CarritosApi()
val id : kotlin.String = id_example // kotlin.String | 
val productoId : kotlin.String = productoId_example // kotlin.String | 
try {
    apiInstance.removeItem(id, productoId)
} catch (e: ClientException) {
    println("4xx response calling CarritosApi#removeItem")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CarritosApi#removeItem")
    e.printStackTrace()
}
```

### Parameters
| **id** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **productoId** | **kotlin.String**|  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

