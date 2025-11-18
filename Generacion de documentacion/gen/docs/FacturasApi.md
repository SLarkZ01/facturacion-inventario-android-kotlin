# FacturasApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**anularFactura**](FacturasApi.md#anularFactura) | **POST** /api/facturas/{id}/anular | Anular factura |
| [**checkout**](FacturasApi.md#checkout) | **POST** /api/facturas/checkout | Checkout carrito |
| [**crearBorrador**](FacturasApi.md#crearBorrador) | **POST** /api/facturas/borrador | Crear factura en BORRADOR |
| [**crearFactura**](FacturasApi.md#crearFactura) | **POST** /api/facturas | Crear factura EMITIDA |
| [**descargarPdf**](FacturasApi.md#descargarPdf) | **GET** /api/facturas/{id}/pdf | Descargar PDF de factura con IVA |
| [**eliminarBorrador**](FacturasApi.md#eliminarBorrador) | **DELETE** /api/facturas/{id} | Eliminar factura borrador |
| [**emitirBorrador**](FacturasApi.md#emitirBorrador) | **POST** /api/facturas/{id}/emitir | Emitir borrador |
| [**getFactura**](FacturasApi.md#getFactura) | **GET** /api/facturas/{id} | Obtener factura por id |
| [**getPorNumero**](FacturasApi.md#getPorNumero) | **GET** /api/facturas/numero/{numero} | Obtener factura por número |
| [**listarPorUsuario**](FacturasApi.md#listarPorUsuario) | **GET** /api/facturas | Listar facturas por usuario |


<a id="anularFactura"></a>
# **anularFactura**
> Factura anularFactura(id)

Anular factura

Anula una factura emitida (NO devuelve stock automáticamente)

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    val result : Factura = apiInstance.anularFactura(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling FacturasApi#anularFactura")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FacturasApi#anularFactura")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.String**|  | |

### Return type

[**Factura**](Factura.md)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="checkout"></a>
# **checkout**
> Factura checkout(requestBody)

Checkout carrito

Crea factura EMITIDA desde carrito. SIEMPRE descuenta stock y calcula precios/IVA desde productos.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val requestBody : kotlin.collections.Map<kotlin.String, kotlin.Any> = Object // kotlin.collections.Map<kotlin.String, kotlin.Any> | 
try {
    val result : Factura = apiInstance.checkout(requestBody)
    println(result)
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
| **requestBody** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md)|  | |

### Return type

[**Factura**](Factura.md)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="crearBorrador"></a>
# **crearBorrador**
> Factura crearBorrador(facturaRequest)

Crear factura en BORRADOR

Crea factura sin descontar stock (para cotizaciones). Usar POST /facturas/{id}/emitir para emitirla.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val facturaRequest : FacturaRequest =  // FacturaRequest | 
try {
    val result : Factura = apiInstance.crearBorrador(facturaRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling FacturasApi#crearBorrador")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FacturasApi#crearBorrador")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **facturaRequest** | [**FacturaRequest**](FacturaRequest.md)|  | |

### Return type

[**Factura**](Factura.md)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="crearFactura"></a>
# **crearFactura**
> Factura crearFactura(facturaRequest)

Crear factura EMITIDA

Crea y emite una factura definitiva. SIEMPRE descuenta stock y calcula precios/IVA desde productos. No acepta precios del cliente.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val facturaRequest : FacturaRequest =  // FacturaRequest | 
try {
    val result : Factura = apiInstance.crearFactura(facturaRequest)
    println(result)
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
| **facturaRequest** | [**FacturaRequest**](FacturaRequest.md)|  | |

### Return type

[**Factura**](Factura.md)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="descargarPdf"></a>
# **descargarPdf**
> java.io.File descargarPdf(id)

Descargar PDF de factura con IVA

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    val result : java.io.File = apiInstance.descargarPdf(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling FacturasApi#descargarPdf")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FacturasApi#descargarPdf")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.String**|  | |

### Return type

[**java.io.File**](java.io.File.md)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a id="eliminarBorrador"></a>
# **eliminarBorrador**
> eliminarBorrador(id)

Eliminar factura borrador

Elimina una factura en estado BORRADOR (cotización rechazada o descartada). Solo se pueden eliminar facturas que NO han sido emitidas.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    apiInstance.eliminarBorrador(id)
} catch (e: ClientException) {
    println("4xx response calling FacturasApi#eliminarBorrador")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FacturasApi#eliminarBorrador")
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
 - **Accept**: application/json

<a id="emitirBorrador"></a>
# **emitirBorrador**
> Factura emitirBorrador(id)

Emitir borrador

Emite un borrador (descuenta stock y cambia estado a EMITIDA)

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    val result : Factura = apiInstance.emitirBorrador(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling FacturasApi#emitirBorrador")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling FacturasApi#emitirBorrador")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.String**|  | |

### Return type

[**Factura**](Factura.md)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="getFactura"></a>
# **getFactura**
> Factura getFactura(id)

Obtener factura por id

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    val result : Factura = apiInstance.getFactura(id)
    println(result)
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

[**Factura**](Factura.md)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="getPorNumero"></a>
# **getPorNumero**
> Factura getPorNumero(numero)

Obtener factura por número

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val numero : kotlin.String = numero_example // kotlin.String | 
try {
    val result : Factura = apiInstance.getPorNumero(numero)
    println(result)
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

[**Factura**](Factura.md)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="listarPorUsuario"></a>
# **listarPorUsuario**
> kotlin.collections.List&lt;Factura&gt; listarPorUsuario(userId)

Listar facturas por usuario

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = FacturasApi()
val userId : kotlin.String = userId_example // kotlin.String | 
try {
    val result : kotlin.collections.List<Factura> = apiInstance.listarPorUsuario(userId)
    println(result)
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

[**kotlin.collections.List&lt;Factura&gt;**](Factura.md)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

