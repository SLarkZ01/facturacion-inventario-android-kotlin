# AuthApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**login**](AuthApi.md#login) | **POST** /api/auth/login | Login |
| [**logout**](AuthApi.md#logout) | **POST** /api/auth/logout | Logout |
| [**me**](AuthApi.md#me) | **GET** /api/auth/me | Obtener info del usuario actual |
| [**oauthGoogle**](AuthApi.md#oauthGoogle) | **POST** /api/auth/oauth/google | OAuth Google |
| [**refresh**](AuthApi.md#refresh) | **POST** /api/auth/refresh | Refresh token |
| [**register**](AuthApi.md#register) | **POST** /api/auth/register | Registro de usuario |
| [**revokeAll**](AuthApi.md#revokeAll) | **POST** /api/auth/revoke-all | Revocar todos los refresh tokens |


<a id="login"></a>
# **login**
> login(loginRequest)

Login

Inicia sesión con username/email y contraseña. Devuelve tokens JWT de acceso y refresh.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = AuthApi()
val loginRequest : LoginRequest = {"usernameOrEmail":"jperez","password":"securePass123","device":"web"} // LoginRequest | Credenciales de acceso
try {
    apiInstance.login(loginRequest)
} catch (e: ClientException) {
    println("4xx response calling AuthApi#login")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthApi#login")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **loginRequest** | [**LoginRequest**](LoginRequest.md)| Credenciales de acceso | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="logout"></a>
# **logout**
> logout(requestBody)

Logout

Revoca el refresh token dado

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = AuthApi()
val requestBody : kotlin.collections.Map<kotlin.String, kotlin.String> =  // kotlin.collections.Map<kotlin.String, kotlin.String> | 
try {
    apiInstance.logout(requestBody)
} catch (e: ClientException) {
    println("4xx response calling AuthApi#logout")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthApi#logout")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestBody** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.String&gt;**](kotlin.String.md)|  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="me"></a>
# **me**
> me()

Obtener info del usuario actual

Devuelve datos del usuario autenticado

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = AuthApi()
try {
    apiInstance.me()
} catch (e: ClientException) {
    println("4xx response calling AuthApi#me")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthApi#me")
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

<a id="oauthGoogle"></a>
# **oauthGoogle**
> oauthGoogle(requestBody)

OAuth Google

Autenticación/registro usando Google OAuth. Requiere el idToken obtenido del flujo OAuth de Google.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = AuthApi()
val requestBody : kotlin.collections.Map<kotlin.String, kotlin.String> = {"idToken":"eyJhbGciOiJSUzI1NiIsImtpZCI6IjAx...","inviteCode":"TALLER-ABC123","device":"android"} // kotlin.collections.Map<kotlin.String, kotlin.String> | Token de Google y datos opcionales
try {
    apiInstance.oauthGoogle(requestBody)
} catch (e: ClientException) {
    println("4xx response calling AuthApi#oauthGoogle")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthApi#oauthGoogle")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestBody** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.String&gt;**](kotlin.String.md)| Token de Google y datos opcionales | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="refresh"></a>
# **refresh**
> refresh(requestBody)

Refresh token

Renueva el token de acceso usando un refresh token válido

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = AuthApi()
val requestBody : kotlin.collections.Map<kotlin.String, kotlin.String> = {"refreshToken":"r1234567890abcdef"} // kotlin.collections.Map<kotlin.String, kotlin.String> | 
try {
    apiInstance.refresh(requestBody)
} catch (e: ClientException) {
    println("4xx response calling AuthApi#refresh")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthApi#refresh")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestBody** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.String&gt;**](kotlin.String.md)|  | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="register"></a>
# **register**
> register(registerRequest)

Registro de usuario

Registra un usuario nuevo en el sistema. Opcionalmente puede incluir un código de invitación para unirse a un taller.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = AuthApi()
val registerRequest : RegisterRequest = {"username":"jperez","email":"jperez@example.com","password":"securePass123","nombre":"Juan","apellido":"Pérez","inviteCode":"ABC123"} // RegisterRequest | Datos del nuevo usuario
try {
    apiInstance.register(registerRequest)
} catch (e: ClientException) {
    println("4xx response calling AuthApi#register")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthApi#register")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **registerRequest** | [**RegisterRequest**](RegisterRequest.md)| Datos del nuevo usuario | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="revokeAll"></a>
# **revokeAll**
> revokeAll()

Revocar todos los refresh tokens

Revoca todos los refresh tokens del usuario autenticado

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = AuthApi()
try {
    apiInstance.revokeAll()
} catch (e: ClientException) {
    println("4xx response calling AuthApi#revokeAll")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthApi#revokeAll")
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

