
# ProductoRequest

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **nombre** | **kotlin.String** |  |  |
| **idString** | **kotlin.String** |  |  [optional] |
| **descripcion** | **kotlin.String** |  |  [optional] |
| **precio** | **kotlin.Double** |  |  [optional] |
| **tasaIva** | **kotlin.Double** |  |  [optional] |
| **stock** | **kotlin.Int** | Stock inicial del producto (opcional). MODO SIMPLE: Si se especifica, el stock se almacena directamente en el producto y las facturas descuentan de aquí. MODO AVANZADO: Para gestión multi-almacén, crear el producto con stock&#x3D;0 y luego usar POST /api/stock/set para asignar a almacenes específicos. El sistema detecta automáticamente qué modo usar al facturar. |  [optional] |
| **categoriaId** | **kotlin.String** |  |  [optional] |
| **listaMedios** | **kotlin.collections.List&lt;kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;&gt;** |  |  [optional] |
| **specs** | **kotlin.collections.Map&lt;kotlin.String, kotlin.String&gt;** |  |  [optional] |
| **tallerId** | **kotlin.String** |  |  [optional] |



