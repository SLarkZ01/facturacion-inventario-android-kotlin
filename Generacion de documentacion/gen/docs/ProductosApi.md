# ProductosApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**actualizarProducto**](ProductosApi.md#actualizarProducto) | **PUT** /api/productos/{id} | Actualizar producto |
| [**ajustarStock**](ProductosApi.md#ajustarStock) | **PATCH** /api/productos/{id}/stock | Ajustar stock de producto |
| [**crearProducto**](ProductosApi.md#crearProducto) | **POST** /api/productos | Crear producto |
| [**eliminar**](ProductosApi.md#eliminar) | **DELETE** /api/productos/{id} | Eliminar producto |
| [**getProducto**](ProductosApi.md#getProducto) | **GET** /api/productos/{id} | Obtener producto por ID |
| [**listar**](ProductosApi.md#listar) | **GET** /api/productos | Listar productos |


<a id="actualizarProducto"></a>
# **actualizarProducto**
> actualizarProducto(id, productoRequest)

Actualizar producto

Actualiza los datos del producto. Envía solo los campos que deseas actualizar.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ProductosApi()
val id : kotlin.String = 507f191e810c19729de860ea // kotlin.String | ID del producto
val productoRequest : ProductoRequest = {"nombre":"Filtro de Aceite Yamaha R15","precio":27.0,"stock":150} // ProductoRequest | Datos actualizados del producto
try {
    apiInstance.actualizarProducto(id, productoRequest)
} catch (e: ClientException) {
    println("4xx response calling ProductosApi#actualizarProducto")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ProductosApi#actualizarProducto")
    e.printStackTrace()
}
```

### Parameters
| **id** | **kotlin.String**| ID del producto | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **productoRequest** | [**ProductoRequest**](ProductoRequest.md)| Datos actualizados del producto | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="ajustarStock"></a>
# **ajustarStock**
> ajustarStock(id, requestBody)

Ajustar stock de producto

Ajusta el stock del producto sumando o restando una cantidad (delta). Usa valores positivos para aumentar y negativos para disminuir.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ProductosApi()
val id : kotlin.String = 507f191e810c19729de860ea // kotlin.String | ID del producto
val requestBody : kotlin.collections.Map<kotlin.String, kotlin.Any> = {"delta":10} // kotlin.collections.Map<kotlin.String, kotlin.Any> | Delta de ajuste de stock
try {
    apiInstance.ajustarStock(id, requestBody)
} catch (e: ClientException) {
    println("4xx response calling ProductosApi#ajustarStock")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ProductosApi#ajustarStock")
    e.printStackTrace()
}
```

### Parameters
| **id** | **kotlin.String**| ID del producto | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestBody** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md)| Delta de ajuste de stock | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="crearProducto"></a>
# **crearProducto**
> crearProducto(productoRequest)

Crear producto

Crea un nuevo producto en el inventario. Para imágenes se recomienda subir a Cloudinary desde el cliente usando el endpoint de firma y enviar en &#x60;listaMedios&#x60; objetos con campos &#x60;publicId&#x60; y &#x60;secure_url&#x60;.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ProductosApi()
val productoRequest : ProductoRequest = {"nombre":"Filtro de Aceite Yamaha","descripcion":"Filtro de aceite para motos Yamaha 150cc","precio":25.5,"stock":100,"categoriaId":"507f1f77bcf86cd799439011","tallerId":"507f1f77bcf86cd799439777","listaMedios":[{"type":"image","publicId":"products/507f1f77/abc123","secure_url":"https://res.cloudinary.com/df7ggzasi/image/upload/v1/products/abc123.jpg","format":"jpg","order":0}],"specs":{"Marca":"Yamaha","Modelo":"YZF-R15"}} // ProductoRequest | Datos del producto (incluir `tallerId`). `listaMedios` ejemplo incluido.
try {
    apiInstance.crearProducto(productoRequest)
} catch (e: ClientException) {
    println("4xx response calling ProductosApi#crearProducto")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ProductosApi#crearProducto")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **productoRequest** | [**ProductoRequest**](ProductoRequest.md)| Datos del producto (incluir &#x60;tallerId&#x60;). &#x60;listaMedios&#x60; ejemplo incluido. | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="eliminar"></a>
# **eliminar**
> eliminar(id)

Eliminar producto

Elimina un producto por ID

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ProductosApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    apiInstance.eliminar(id)
} catch (e: ClientException) {
    println("4xx response calling ProductosApi#eliminar")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ProductosApi#eliminar")
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

<a id="getProducto"></a>
# **getProducto**
> getProducto(id)

Obtener producto por ID

Devuelve los detalles completos de un producto (incluye &#x60;listaMedios&#x60; si existen)

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ProductosApi()
val id : kotlin.String = 507f191e810c19729de860ea // kotlin.String | ID del producto
try {
    apiInstance.getProducto(id)
} catch (e: ClientException) {
    println("4xx response calling ProductosApi#getProducto")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ProductosApi#getProducto")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.String**| ID del producto | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="listar"></a>
# **listar**
> listar(q, categoriaId, tallerId, page, size)

Listar productos

Devuelve una lista paginada de productos. Soporta búsqueda por nombre (q) o filtrado por categoría (categoriaId).

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ProductosApi()
val q : kotlin.String = filtro // kotlin.String | Búsqueda por nombre de producto
val categoriaId : kotlin.String = 507f1f77bcf86cd799439011 // kotlin.String | ID de la categoría para filtrar
val tallerId : kotlin.String = 507f1f77bcf86cd799439777 // kotlin.String | ID del taller/tienda para filtrar productos por tienda
val page : kotlin.Int = 0 // kotlin.Int | Número de página (base 0)
val size : kotlin.Int = 20 // kotlin.Int | Cantidad de elementos por página
try {
    apiInstance.listar(q, categoriaId, tallerId, page, size)
} catch (e: ClientException) {
    println("4xx response calling ProductosApi#listar")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ProductosApi#listar")
    e.printStackTrace()
}
```

### Parameters
| **q** | **kotlin.String**| Búsqueda por nombre de producto | [optional] |
| **categoriaId** | **kotlin.String**| ID de la categoría para filtrar | [optional] |
| **tallerId** | **kotlin.String**| ID del taller/tienda para filtrar productos por tienda | [optional] |
| **page** | **kotlin.Int**| Número de página (base 0) | [optional] [default to 0] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **size** | **kotlin.Int**| Cantidad de elementos por página | [optional] [default to 20] |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

