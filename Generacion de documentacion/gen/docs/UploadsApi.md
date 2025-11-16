# UploadsApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**sign**](UploadsApi.md#sign) | **POST** /api/uploads/cloudinary-sign | Generar firma para Cloudinary |


<a id="sign"></a>
# **sign**
> kotlin.Any sign()

Generar firma para Cloudinary

Genera apiKey, timestamp y signature para permitir uploads firmados desde el cliente. El body opcional puede incluir parámetros a firmar como &#39;folder&#39; o &#39;public_id&#39;.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = UploadsApi()
try {
    val result : kotlin.Any = apiInstance.sign()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UploadsApi#sign")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UploadsApi#sign")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.Any**](kotlin.Any.md)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

