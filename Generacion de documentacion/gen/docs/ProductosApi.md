# ProductosApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**actualizarProducto**](ProductosApi.md#actualizarProducto) | **PUT** /api/productos/{id} | Actualizar producto |
| [**ajustarStock**](ProductosApi.md#ajustarStock) | **PATCH** /api/productos/{id}/stock | Ajustar stock de producto |
| [**crearProducto**](ProductosApi.md#crearProducto) | **POST** /api/productos | Crear producto |
| [**eliminar**](ProductosApi.md#eliminar) | **DELETE** /api/productos/{id} | Eliminar producto |
| [**getProducto1**](ProductosApi.md#getProducto1) | **GET** /api/productos/{id} | Obtener producto por ID |
| [**listar1**](ProductosApi.md#listar1) | **GET** /api/productos | Listar productos |


<a id="actualizarProducto"></a>
# **actualizarProducto**
> actualizarProducto(id, productoRequest)

Actualizar producto

Actualiza los datos del producto. Envía solo los campos que deseas actualizar.  **IMPORTANTE sobre &#x60;listaMedios&#x60;:** - Si actualizas &#x60;listaMedios&#x60;, DEBE incluir &#x60;publicId&#x60; en cada medio - Al actualizar, se reemplazan las imágenes anteriores (las viejas se eliminan de Cloudinary automáticamente) - Para agregar nuevas imágenes manteniendo las existentes, primero obtén el producto actual y agrega a su array  **Nota sobre &#x60;specs&#x60;:** - Al actualizar &#x60;specs&#x60;, se reemplaza el objeto completo (no se hace merge) - Para actualizar una sola especificación, envía el objeto completo con el cambio 

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ProductosApi()
val id : kotlin.String = 507f191e810c19729de860ea // kotlin.String | ID del producto
val productoRequest : ProductoRequest = {"nombre":"Filtro de Aceite Yamaha R15","precio":27.0,"stock":150} // ProductoRequest | Datos actualizados del producto (solo enviar los campos a modificar). Si actualizas `listaMedios`, DEBE incluir `publicId` en cada elemento. 
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
| **productoRequest** | [**ProductoRequest**](ProductoRequest.md)| Datos actualizados del producto (solo enviar los campos a modificar). Si actualizas &#x60;listaMedios&#x60;, DEBE incluir &#x60;publicId&#x60; en cada elemento.  | |

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

Crea un nuevo producto en el inventario.  **NUEVO (v2.0): Campo tasaIva obligatorio para facturación** - &#x60;tasaIva&#x60;: Tasa de IVA en porcentaje (ej: 0, 5, 19). **Default: 19%** si no se especifica - Este campo es usado automáticamente al generar facturas para calcular el IVA - Valores comunes en Colombia: 0% (exento), 5% (canasta básica), 19% (estándar)  **IMPORTANTE - Gestión de Imágenes:** - Las imágenes DEBEN subirse primero a Cloudinary usando &#x60;/api/uploads/cloudinary-sign&#x60; - Cada objeto en &#x60;listaMedios&#x60; DEBE incluir el campo &#x60;publicId&#x60; (CRÍTICO para eliminar imágenes al borrar el producto) - Sin &#x60;publicId&#x60;, las imágenes quedarán huérfanas en Cloudinary y no se podrán eliminar automáticamente  **Campos recomendados:** - &#x60;tallerId&#x60;: Asocia el producto a un taller específico (recomendado para multi-tenant) - &#x60;specs&#x60;: Mapa de especificaciones técnicas (ej: {\&quot;Marca\&quot;:\&quot;Yamaha\&quot;, \&quot;Modelo\&quot;:\&quot;R15\&quot;, \&quot;Peso\&quot;:\&quot;0.2kg\&quot;}) - &#x60;listaMedios&#x60;: Array de objetos con estructura: {type, publicId, secure_url, format, width, height, order}  **Flujo recomendado para imágenes:** 1. Obtener firma: POST /api/uploads/cloudinary-sign con {folder: \&quot;products\&quot;} 2. Subir a Cloudinary con la firma obtenida 3. Guardar en &#x60;listaMedios&#x60; el objeto completo que devuelve Cloudinary (especialmente &#x60;public_id&#x60;) 

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ProductosApi()
val productoRequest : ProductoRequest = {"nombre":"Filtro de Aceite Yamaha","descripcion":"Filtro de aceite para motos Yamaha 150cc","precio":25000,"tasaIva":19.0,"stock":100,"categoriaId":"507f1f77bcf86cd799439011","tallerId":"507f1f77bcf86cd799439777","listaMedios":[{"type":"image/jpeg","publicId":"products/507f1f77/filtro-yamaha-abc123","secure_url":"https://res.cloudinary.com/df7ggzasi/image/upload/v1763285023/products/507f1f77/filtro-yamaha-abc123.jpg","url":"https://res.cloudinary.com/df7ggzasi/image/upload/v1763285023/products/507f1f77/filtro-yamaha-abc123.jpg","format":"jpg","width":800,"height":600,"order":0}],"specs":{"Marca":"Yamaha","Modelo":"YZF-R15","Compatibilidad":"150cc","Material":"Papel","Peso":"0.2kg"}} // ProductoRequest | Datos del producto. **NUEVO**: Campo `tasaIva` (IVA en %) - Si no se envía, se asigna 19% por defecto. **CRÍTICO**: Si incluyes `listaMedios`, cada medio DEBE tener `publicId` para poder eliminar las imágenes de Cloudinary. El campo `specs` es opcional pero recomendado para especificaciones técnicas. 
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
| **productoRequest** | [**ProductoRequest**](ProductoRequest.md)| Datos del producto. **NUEVO**: Campo &#x60;tasaIva&#x60; (IVA en %) - Si no se envía, se asigna 19% por defecto. **CRÍTICO**: Si incluyes &#x60;listaMedios&#x60;, cada medio DEBE tener &#x60;publicId&#x60; para poder eliminar las imágenes de Cloudinary. El campo &#x60;specs&#x60; es opcional pero recomendado para especificaciones técnicas.  | |

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

Elimina un producto por ID. **IMPORTANTE**: También elimina automáticamente las imágenes asociadas de Cloudinary (si existen en &#x60;listaMedios&#x60;).

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ProductosApi()
val id : kotlin.String = 507f191e810c19729de860ea // kotlin.String | ID del producto a eliminar
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
| **id** | **kotlin.String**| ID del producto a eliminar | |

### Return type

null (empty response body)

### Authorization


Configure bearerAuth:
    ApiClient.accessToken = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="getProducto1"></a>
# **getProducto1**
> getProducto1(id)

Obtener producto por ID

Devuelve los detalles completos de un producto.  **La respuesta incluye:** - Datos básicos (nombre, descripción, precio, stock) - **tasaIva**: Tasa de IVA en porcentaje (usado para calcular IVA en facturas) - &#x60;listaMedios&#x60;: Array con imágenes (cada una con publicId, secure_url, etc.) - &#x60;specs&#x60;: Objeto con especificaciones técnicas (si existen) - &#x60;tallerId&#x60;: ID del taller propietario (si existe) - &#x60;categoriaId&#x60;: ID de la categoría (si existe) 

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiInstance = ProductosApi()
val id : kotlin.String = 507f191e810c19729de860ea // kotlin.String | ID del producto
try {
    apiInstance.getProducto1(id)
} catch (e: ClientException) {
    println("4xx response calling ProductosApi#getProducto1")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ProductosApi#getProducto1")
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

<a id="listar1"></a>
# **listar1**
> listar1(q, categoriaId, tallerId, page, size)

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
    apiInstance.listar1(q, categoriaId, tallerId, page, size)
} catch (e: ClientException) {
    println("4xx response calling ProductosApi#listar1")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ProductosApi#listar1")
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

