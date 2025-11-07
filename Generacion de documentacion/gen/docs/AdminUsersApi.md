# AdminUsersApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**createAdmin**](AdminUsersApi.md#createAdmin) | **POST** /api/admin/users | Crear administrador |


<a id="createAdmin"></a>
# **createAdmin**
> createAdmin(createAdminRequest)

Crear administrador

Crea un nuevo usuario con rol de administrador. Solo puede ser ejecutado por usuarios que ya son administradores.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = AdminUsersApi()
val createAdminRequest : CreateAdminRequest = {"username":"admin.sistemas","email":"admin@empresa.com","password":"SecurePass123!","nombre":"Juan","apellido":"Administrador"} // CreateAdminRequest | Datos del nuevo administrador
try {
    apiInstance.createAdmin(createAdminRequest)
} catch (e: ClientException) {
    println("4xx response calling AdminUsersApi#createAdmin")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AdminUsersApi#createAdmin")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **createAdminRequest** | [**CreateAdminRequest**](CreateAdminRequest.md)| Datos del nuevo administrador | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

