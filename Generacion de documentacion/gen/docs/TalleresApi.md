# TalleresApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**acceptInvitacion**](TalleresApi.md#acceptInvitacion) | **POST** /api/talleres/invitaciones/accept | Aceptar invitación por código |
| [**actualizarAlmacen**](TalleresApi.md#actualizarAlmacen) | **PUT** /api/talleres/{tallerId}/almacenes/{almacenId} | Actualizar almacén |
| [**actualizarTaller**](TalleresApi.md#actualizarTaller) | **PUT** /api/talleres/{tallerId} | Actualizar taller |
| [**crearAlmacen**](TalleresApi.md#crearAlmacen) | **POST** /api/talleres/{tallerId}/almacenes | Crear almacén en taller |
| [**crearInvitacionCodigo**](TalleresApi.md#crearInvitacionCodigo) | **POST** /api/talleres/{tallerId}/invitaciones/codigo | Crear invitación por código |
| [**crearTaller**](TalleresApi.md#crearTaller) | **POST** /api/talleres | Crear taller |
| [**debugListByUser**](TalleresApi.md#debugListByUser) | **GET** /api/talleres/_debug | DEBUG: listar talleres por userId (temporal) |
| [**eliminarAlmacen**](TalleresApi.md#eliminarAlmacen) | **DELETE** /api/talleres/{tallerId}/almacenes/{almacenId} | Eliminar almacén |
| [**eliminarTaller**](TalleresApi.md#eliminarTaller) | **DELETE** /api/talleres/{tallerId} | Eliminar taller |
| [**getAlmacen**](TalleresApi.md#getAlmacen) | **GET** /api/talleres/{tallerId}/almacenes/{almacenId} | Obtener almacén |
| [**getTaller**](TalleresApi.md#getTaller) | **GET** /api/talleres/{tallerId} | Obtener taller |
| [**listMyTalleres**](TalleresApi.md#listMyTalleres) | **GET** /api/talleres | Listar talleres propios |
| [**listarAlmacenes**](TalleresApi.md#listarAlmacenes) | **GET** /api/talleres/{tallerId}/almacenes | Listar almacenes de un taller |
| [**listarMiembros**](TalleresApi.md#listarMiembros) | **GET** /api/talleres/{tallerId}/miembros | Listar miembros del taller |


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

<a id="actualizarAlmacen"></a>
# **actualizarAlmacen**
> actualizarAlmacen(tallerId, almacenId, almacenRequest)

Actualizar almacén

Actualiza un almacén (nombre/ubicacion). Requiere owner o ADMIN.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TalleresApi()
val tallerId : kotlin.String = tallerId_example // kotlin.String | 
val almacenId : kotlin.String = almacenId_example // kotlin.String | 
val almacenRequest : AlmacenRequest = {"nombre":"Almacén Nuevo","ubicacion":"Calle 1"} // AlmacenRequest | Datos de almacén
try {
    apiInstance.actualizarAlmacen(tallerId, almacenId, almacenRequest)
} catch (e: ClientException) {
    println("4xx response calling TalleresApi#actualizarAlmacen")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TalleresApi#actualizarAlmacen")
    e.printStackTrace()
}
```

### Parameters
| **tallerId** | **kotlin.String**|  | |
| **almacenId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **almacenRequest** | [**AlmacenRequest**](AlmacenRequest.md)| Datos de almacén | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="actualizarTaller"></a>
# **actualizarTaller**
> actualizarTaller(tallerId, tallerRequest)

Actualizar taller

Actualiza datos del taller (solo nombre por ahora)

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TalleresApi()
val tallerId : kotlin.String = tallerId_example // kotlin.String | 
val tallerRequest : TallerRequest = {"nombre":"Nuevo Nombre"} // TallerRequest | Datos a actualizar
try {
    apiInstance.actualizarTaller(tallerId, tallerRequest)
} catch (e: ClientException) {
    println("4xx response calling TalleresApi#actualizarTaller")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TalleresApi#actualizarTaller")
    e.printStackTrace()
}
```

### Parameters
| **tallerId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **tallerRequest** | [**TallerRequest**](TallerRequest.md)| Datos a actualizar | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

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
> crearTaller(tallerRequest)

Crear taller

Crea un taller y lo asocia al usuario autenticado como propietario (owner)

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TalleresApi()
val tallerRequest : TallerRequest = {"nombre":"Taller Motos del Norte"} // TallerRequest | Datos del taller
try {
    apiInstance.crearTaller(tallerRequest)
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
| **tallerRequest** | [**TallerRequest**](TallerRequest.md)| Datos del taller | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="debugListByUser"></a>
# **debugListByUser**
> debugListByUser(userId)

DEBUG: listar talleres por userId (temporal)

Endpoint temporal para depuración: pasar ?userId&#x3D;&lt;id&gt; para simular autenticación y devolver talleres para ese userId. Eliminar/proteger en producción.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TalleresApi()
val userId : kotlin.String = userId_example // kotlin.String | 
try {
    apiInstance.debugListByUser(userId)
} catch (e: ClientException) {
    println("4xx response calling TalleresApi#debugListByUser")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TalleresApi#debugListByUser")
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

<a id="eliminarAlmacen"></a>
# **eliminarAlmacen**
> eliminarAlmacen(tallerId, almacenId)

Eliminar almacén

Elimina permanentemente un almacén. Requiere owner o ADMIN. Esta acción no se puede deshacer.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TalleresApi()
val tallerId : kotlin.String = tallerId_example // kotlin.String | 
val almacenId : kotlin.String = almacenId_example // kotlin.String | 
try {
    apiInstance.eliminarAlmacen(tallerId, almacenId)
} catch (e: ClientException) {
    println("4xx response calling TalleresApi#eliminarAlmacen")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TalleresApi#eliminarAlmacen")
    e.printStackTrace()
}
```

### Parameters
| **tallerId** | **kotlin.String**|  | |
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

<a id="eliminarTaller"></a>
# **eliminarTaller**
> eliminarTaller(tallerId)

Eliminar taller

Elimina permanentemente el taller y todos sus almacenes asociados. Solo el owner puede hacerlo. Esta acción no se puede deshacer.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TalleresApi()
val tallerId : kotlin.String = tallerId_example // kotlin.String | 
try {
    apiInstance.eliminarTaller(tallerId)
} catch (e: ClientException) {
    println("4xx response calling TalleresApi#eliminarTaller")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TalleresApi#eliminarTaller")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **tallerId** | **kotlin.String**|  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a id="getAlmacen"></a>
# **getAlmacen**
> getAlmacen(tallerId, almacenId)

Obtener almacén

Obtiene un almacén por id

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TalleresApi()
val tallerId : kotlin.String = tallerId_example // kotlin.String | 
val almacenId : kotlin.String = almacenId_example // kotlin.String | 
try {
    apiInstance.getAlmacen(tallerId, almacenId)
} catch (e: ClientException) {
    println("4xx response calling TalleresApi#getAlmacen")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TalleresApi#getAlmacen")
    e.printStackTrace()
}
```

### Parameters
| **tallerId** | **kotlin.String**|  | |
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

<a id="getTaller"></a>
# **getTaller**
> getTaller(tallerId)

Obtener taller

Obtiene información del taller por id

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TalleresApi()
val tallerId : kotlin.String = tallerId_example // kotlin.String | 
try {
    apiInstance.getTaller(tallerId)
} catch (e: ClientException) {
    println("4xx response calling TalleresApi#getTaller")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TalleresApi#getTaller")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **tallerId** | **kotlin.String**|  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

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

<a id="listarAlmacenes"></a>
# **listarAlmacenes**
> listarAlmacenes(tallerId)

Listar almacenes de un taller

Devuelve los almacenes asociados a un taller

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TalleresApi()
val tallerId : kotlin.String = tallerId_example // kotlin.String | 
try {
    apiInstance.listarAlmacenes(tallerId)
} catch (e: ClientException) {
    println("4xx response calling TalleresApi#listarAlmacenes")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TalleresApi#listarAlmacenes")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **tallerId** | **kotlin.String**|  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a id="listarMiembros"></a>
# **listarMiembros**
> listarMiembros(tallerId)

Listar miembros del taller

Devuelve los miembros de un taller (userId, roles, joinedAt). Requiere que el usuario autenticado pertenezca al taller.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TalleresApi()
val tallerId : kotlin.String = tallerId_example // kotlin.String | 
try {
    apiInstance.listarMiembros(tallerId)
} catch (e: ClientException) {
    println("4xx response calling TalleresApi#listarMiembros")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TalleresApi#listarMiembros")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **tallerId** | **kotlin.String**|  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

