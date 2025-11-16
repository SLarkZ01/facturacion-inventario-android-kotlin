# TalleresApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**acceptInvitacion**](TalleresApi.md#acceptInvitacion) | **POST** /api/talleres/invitaciones/accept | Aceptar invitación por código |
| [**crearAlmacen**](TalleresApi.md#crearAlmacen) | **POST** /api/talleres/{tallerId}/almacenes | Crear almacén en taller |
| [**crearInvitacionCodigo**](TalleresApi.md#crearInvitacionCodigo) | **POST** /api/talleres/{tallerId}/invitaciones/codigo | Crear invitación por código |
| [**crearTaller**](TalleresApi.md#crearTaller) | **POST** /api/talleres | Crear taller |
| [**listMyTalleres**](TalleresApi.md#listMyTalleres) | **GET** /api/talleres | Listar talleres propios |


<a id="acceptInvitacion"></a>
# **acceptInvitacion**
> acceptInvitacion(requestBody)

Aceptar invitación por código

Permite a un usuario autenticado unirse a un taller usando un código de invitación válido

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TalleresApi()
val requestBody : kotlin.collections.Map<kotlin.String, kotlin.String> = {"code":"TALLER-ABC123XYZ"} // kotlin.collections.Map<kotlin.String, kotlin.String> | Código de invitación
try {
    apiInstance.acceptInvitacion(requestBody)
} catch (e: ClientException) {
    println("4xx response calling TalleresApi#acceptInvitacion")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TalleresApi#acceptInvitacion")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestBody** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.String&gt;**](kotlin.String.md)| Código de invitación | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="crearAlmacen"></a>
# **crearAlmacen**
> crearAlmacen(tallerId, requestBody)

Crear almacén en taller

Crea un almacén dentro de un taller existente. Solo el owner o administradores del taller pueden crear almacenes.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TalleresApi()
val tallerId : kotlin.String = 507f1f77bcf86cd799439777 // kotlin.String | ID del taller
val requestBody : kotlin.collections.Map<kotlin.String, kotlin.String> = {"nombre":"Almacén Principal","ubicacion":"Calle 123, Local 5"} // kotlin.collections.Map<kotlin.String, kotlin.String> | Datos del almacén
try {
    apiInstance.crearAlmacen(tallerId, requestBody)
} catch (e: ClientException) {
    println("4xx response calling TalleresApi#crearAlmacen")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TalleresApi#crearAlmacen")
    e.printStackTrace()
}
```

### Parameters
| **tallerId** | **kotlin.String**| ID del taller | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestBody** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.String&gt;**](kotlin.String.md)| Datos del almacén | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="crearInvitacionCodigo"></a>
# **crearInvitacionCodigo**
> crearInvitacionCodigo(tallerId, requestBody)

Crear invitación por código

Genera un código de invitación para que usuarios puedan unirse a un taller. Roles disponibles: VENDEDOR, ADMIN.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TalleresApi()
val tallerId : kotlin.String = 507f1f77bcf86cd799439777 // kotlin.String | ID del taller
val requestBody : kotlin.collections.Map<kotlin.String, kotlin.Any> = {"role":"VENDEDOR","daysValid":7} // kotlin.collections.Map<kotlin.String, kotlin.Any> | Configuración de la invitación
try {
    apiInstance.crearInvitacionCodigo(tallerId, requestBody)
} catch (e: ClientException) {
    println("4xx response calling TalleresApi#crearInvitacionCodigo")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TalleresApi#crearInvitacionCodigo")
    e.printStackTrace()
}
```

### Parameters
| **tallerId** | **kotlin.String**| ID del taller | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestBody** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md)| Configuración de la invitación | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="crearTaller"></a>
# **crearTaller**
> crearTaller(requestBody)

Crear taller

Crea un taller y lo asocia al usuario autenticado como propietario (owner)

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TalleresApi()
val requestBody : kotlin.collections.Map<kotlin.String, kotlin.String> = {"nombre":"Taller Motos del Norte"} // kotlin.collections.Map<kotlin.String, kotlin.String> | Datos del taller
try {
    apiInstance.crearTaller(requestBody)
} catch (e: ClientException) {
    println("4xx response calling TalleresApi#crearTaller")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TalleresApi#crearTaller")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestBody** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.String&gt;**](kotlin.String.md)| Datos del taller | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="listMyTalleres"></a>
# **listMyTalleres**
> listMyTalleres()

Listar talleres propios

Lista los talleres donde el usuario es owner

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TalleresApi()
try {
    apiInstance.listMyTalleres()
} catch (e: ClientException) {
    println("4xx response calling TalleresApi#listMyTalleres")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TalleresApi#listMyTalleres")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

