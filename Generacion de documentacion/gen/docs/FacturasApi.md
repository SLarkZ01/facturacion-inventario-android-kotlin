# FacturasApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**checkout**](FacturasApi.md#checkout) | **POST** /api/facturas/checkout | Checkout carrito |
| [**crearFactura**](FacturasApi.md#crearFactura) | **POST** /api/facturas | Crear factura (map) |
| [**crearFacturaDTO**](FacturasApi.md#crearFacturaDTO) | **POST** /api/facturas/dto | Crear factura (DTO) |
| [**getFactura**](FacturasApi.md#getFactura) | **GET** /api/facturas/{id} | Obtener factura por id |
| [**getPorNumero**](FacturasApi.md#getPorNumero) | **GET** /api/facturas/numero/{numero} | Obtener factura por número |
| [**listarPorUsuario**](FacturasApi.md#listarPorUsuario) | **GET** /api/facturas | Listar facturas por usuario |


<a id="checkout"></a>
# **checkout**
> checkout(requestBody)

Checkout carrito

Crea una factura a partir de un carrito del usuario autenticado. Convierte los items del carrito en una factura y actualiza el stock.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val requestBody : kotlin.collections.Map<kotlin.String, kotlin.Any> = {"carritoId":"507f1f77bcf86cd799439999"} // kotlin.collections.Map<kotlin.String, kotlin.Any> | ID del carrito a facturar
try {
    apiInstance.checkout(requestBody)
} catch (e: ClientException) {
    println("4xx response calling FacturasApi#checkout")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FacturasApi#checkout")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestBody** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md)| ID del carrito a facturar | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="crearFactura"></a>
# **crearFactura**
> crearFactura(requestBody)

Crear factura (map)

Crea una factura usando un payload genérico

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val requestBody : kotlin.collections.Map<kotlin.String, kotlin.Any> = Object // kotlin.collections.Map<kotlin.String, kotlin.Any> | 
try {
    apiInstance.crearFactura(requestBody)
} catch (e: ClientException) {
    println("4xx response calling FacturasApi#crearFactura")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FacturasApi#crearFactura")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestBody** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md)|  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="crearFacturaDTO"></a>
# **crearFacturaDTO**
> crearFacturaDTO(facturaRequest)

Crear factura (DTO)

Crea una factura usando DTO tipado

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val facturaRequest : FacturaRequest =  // FacturaRequest | 
try {
    apiInstance.crearFacturaDTO(facturaRequest)
} catch (e: ClientException) {
    println("4xx response calling FacturasApi#crearFacturaDTO")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FacturasApi#crearFacturaDTO")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **facturaRequest** | [**FacturaRequest**](FacturaRequest.md)|  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="getFactura"></a>
# **getFactura**
> getFactura(id)

Obtener factura por id

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    apiInstance.getFactura(id)
} catch (e: ClientException) {
    println("4xx response calling FacturasApi#getFactura")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FacturasApi#getFactura")
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

<a id="getPorNumero"></a>
# **getPorNumero**
> getPorNumero(numero)

Obtener factura por número

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val numero : kotlin.String = numero_example // kotlin.String | 
try {
    apiInstance.getPorNumero(numero)
} catch (e: ClientException) {
    println("4xx response calling FacturasApi#getPorNumero")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FacturasApi#getPorNumero")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **numero** | **kotlin.String**|  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a id="listarPorUsuario"></a>
# **listarPorUsuario**
> listarPorUsuario(userId)

Listar facturas por usuario

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val userId : kotlin.String = userId_example // kotlin.String | 
try {
    apiInstance.listarPorUsuario(userId)
} catch (e: ClientException) {
    println("4xx response calling FacturasApi#listarPorUsuario")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FacturasApi#listarPorUsuario")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.String**|  | [optional] |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

