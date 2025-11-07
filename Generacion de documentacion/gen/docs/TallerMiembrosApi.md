# TallerMiembrosApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**demote**](TallerMiembrosApi.md#demote) | **POST** /api/talleres/{tallerId}/miembros/{memberUserId}/demote | Demover miembro (remover rol ADMIN) |
| [**promote**](TallerMiembrosApi.md#promote) | **POST** /api/talleres/{tallerId}/miembros/{memberUserId}/promote | Promover miembro a ADMIN |
| [**remove**](TallerMiembrosApi.md#remove) | **DELETE** /api/talleres/{tallerId}/miembros/{memberUserId} | Remover miembro del taller |


<a id="demote"></a>
# **demote**
> demote(tallerId, memberUserId)

Demover miembro (remover rol ADMIN)

Remueve los permisos de administrador a un miembro del taller. Solo puede hacerlo el owner.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TallerMiembrosApi()
val tallerId : kotlin.String = 507f1f77bcf86cd799439777 // kotlin.String | ID del taller
val memberUserId : kotlin.String = 507f1f77bcf86cd799439011 // kotlin.String | ID del usuario a demover
try {
    apiInstance.demote(tallerId, memberUserId)
} catch (e: ClientException) {
    println("4xx response calling TallerMiembrosApi#demote")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TallerMiembrosApi#demote")
    e.printStackTrace()
}
```

### Parameters
| **tallerId** | **kotlin.String**| ID del taller | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **memberUserId** | **kotlin.String**| ID del usuario a demover | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="promote"></a>
# **promote**
> promote(tallerId, memberUserId)

Promover miembro a ADMIN

Otorga permisos de administrador a un miembro del taller. Solo puede hacerlo el owner o un miembro ADMIN.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TallerMiembrosApi()
val tallerId : kotlin.String = 507f1f77bcf86cd799439777 // kotlin.String | ID del taller
val memberUserId : kotlin.String = 507f1f77bcf86cd799439011 // kotlin.String | ID del usuario a promover
try {
    apiInstance.promote(tallerId, memberUserId)
} catch (e: ClientException) {
    println("4xx response calling TallerMiembrosApi#promote")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TallerMiembrosApi#promote")
    e.printStackTrace()
}
```

### Parameters
| **tallerId** | **kotlin.String**| ID del taller | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **memberUserId** | **kotlin.String**| ID del usuario a promover | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="remove"></a>
# **remove**
> remove(tallerId, memberUserId)

Remover miembro del taller

Elimina un miembro del taller. Solo puede hacerlo el owner o un ADMIN.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = TallerMiembrosApi()
val tallerId : kotlin.String = 507f1f77bcf86cd799439777 // kotlin.String | ID del taller
val memberUserId : kotlin.String = 507f1f77bcf86cd799439011 // kotlin.String | ID del usuario a remover
try {
    apiInstance.remove(tallerId, memberUserId)
} catch (e: ClientException) {
    println("4xx response calling TallerMiembrosApi#remove")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TallerMiembrosApi#remove")
    e.printStackTrace()
}
```

### Parameters
| **tallerId** | **kotlin.String**| ID del taller | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **memberUserId** | **kotlin.String**| ID del usuario a remover | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

