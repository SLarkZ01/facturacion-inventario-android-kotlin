# StockApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**adjust**](StockApi.md#adjust) | **POST** /api/stock/adjust | Ajustar stock (delta) |
| [**delete**](StockApi.md#delete) | **DELETE** /api/stock | Eliminar registro de stock |
| [**getByProducto**](StockApi.md#getByProducto) | **GET** /api/stock | Obtener stock por producto |
| [**set**](StockApi.md#set) | **PUT** /api/stock/set | Setear stock |


<a id="adjust"></a>
# **adjust**
> adjust(requestBody)

Ajustar stock (delta)

Aumenta o disminuye el stock en un almacén específico. Use valores positivos para aumentar y negativos para disminuir.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = StockApi()
val requestBody : kotlin.collections.Map<kotlin.String, kotlin.Any> = {"productoId":"507f191e810c19729de860ea","almacenId":"507faaa1bcf86cd799439011","delta":50} // kotlin.collections.Map<kotlin.String, kotlin.Any> | Datos del ajuste
try {
    apiInstance.adjust(requestBody)
} catch (e: ClientException) {
    println("4xx response calling StockApi#adjust")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling StockApi#adjust")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestBody** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md)| Datos del ajuste | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="delete"></a>
# **delete**
> delete(productoId, almacenId)

Eliminar registro de stock

Elimina el registro de stock para producto+almacén

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = StockApi()
val productoId : kotlin.String = productoId_example // kotlin.String | 
val almacenId : kotlin.String = almacenId_example // kotlin.String | 
try {
    apiInstance.delete(productoId, almacenId)
} catch (e: ClientException) {
    println("4xx response calling StockApi#delete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling StockApi#delete")
    e.printStackTrace()
}
```

### Parameters
| **productoId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **almacenId** | **kotlin.String**|  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a id="getByProducto"></a>
# **getByProducto**
> getByProducto(productoId)

Obtener stock por producto

Devuelve el stock disponible de un producto desglosado por almacén y el total consolidado

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = StockApi()
val productoId : kotlin.String = 507f191e810c19729de860ea // kotlin.String | ID del producto
try {
    apiInstance.getByProducto(productoId)
} catch (e: ClientException) {
    println("4xx response calling StockApi#getByProducto")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling StockApi#getByProducto")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **productoId** | **kotlin.String**| ID del producto | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="set"></a>
# **set**
> set(requestBody)

Setear stock

Establece la cantidad exacta de stock en un almacén específico (reemplaza el valor anterior)

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = StockApi()
val requestBody : kotlin.collections.Map<kotlin.String, kotlin.Any> = {"productoId":"507f191e810c19729de860ea","almacenId":"507faaa1bcf86cd799439011","cantidad":100} // kotlin.collections.Map<kotlin.String, kotlin.Any> | Cantidad exacta a establecer
try {
    apiInstance.set(requestBody)
} catch (e: ClientException) {
    println("4xx response calling StockApi#set")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling StockApi#set")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestBody** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md)| Cantidad exacta a establecer | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

