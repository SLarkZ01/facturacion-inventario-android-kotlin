# MovimientosApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**crearMovimiento**](MovimientosApi.md#crearMovimiento) | **POST** /api/movimientos | Crear movimiento |
| [**getMovimiento**](MovimientosApi.md#getMovimiento) | **GET** /api/movimientos/{id} | Obtener movimiento por id |
| [**listar1**](MovimientosApi.md#listar1) | **GET** /api/movimientos | Listar movimientos |


<a id="crearMovimiento"></a>
# **crearMovimiento**
> crearMovimiento(movimientoRequest)

Crear movimiento

Registra una entrada (INGRESO) o salida (EGRESO) de stock. Los tipos válidos son: INGRESO, EGRESO, VENTA, DEVOLUCION, AJUSTE.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = MovimientosApi()
val movimientoRequest : MovimientoRequest = {"tipo":"INGRESO","productoId":"507f191e810c19729de860ea","cantidad":50,"referencia":"OC-2024-001","notas":"Compra a proveedor","realizadoPor":"507f1f77bcf86cd799439011"} // MovimientoRequest | Datos del movimiento
try {
    apiInstance.crearMovimiento(movimientoRequest)
} catch (e: ClientException) {
    println("4xx response calling MovimientosApi#crearMovimiento")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MovimientosApi#crearMovimiento")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **movimientoRequest** | [**MovimientoRequest**](MovimientoRequest.md)| Datos del movimiento | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="getMovimiento"></a>
# **getMovimiento**
> getMovimiento(id)

Obtener movimiento por id

Devuelve un movimiento por su id

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = MovimientosApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    apiInstance.getMovimiento(id)
} catch (e: ClientException) {
    println("4xx response calling MovimientosApi#getMovimiento")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MovimientosApi#getMovimiento")
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

<a id="listar1"></a>
# **listar1**
> listar1(productoId, tipo)

Listar movimientos

Lista movimientos de stock. Puede filtrarse por producto o por tipo de movimiento.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = MovimientosApi()
val productoId : kotlin.String = 507f191e810c19729de860ea // kotlin.String | ID del producto para filtrar
val tipo : kotlin.String = INGRESO // kotlin.String | Tipo de movimiento (INGRESO, EGRESO, VENTA, DEVOLUCION, AJUSTE)
try {
    apiInstance.listar1(productoId, tipo)
} catch (e: ClientException) {
    println("4xx response calling MovimientosApi#listar1")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MovimientosApi#listar1")
    e.printStackTrace()
}
```

### Parameters
| **productoId** | **kotlin.String**| ID del producto para filtrar | [optional] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **tipo** | **kotlin.String**| Tipo de movimiento (INGRESO, EGRESO, VENTA, DEVOLUCION, AJUSTE) | [optional] |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

