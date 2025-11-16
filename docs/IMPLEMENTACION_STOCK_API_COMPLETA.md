# ✅ Implementación Completa de Stock API en Android

## 📋 Resumen de Implementación

Se ha implementado completamente la integración de la API de Stock del backend en la aplicación Android con Kotlin y Jetpack Compose.

---

## 🗂️ Archivos Creados/Modificados

### 1. **Modelos de Datos (DTOs)**
📁 `app/src/main/java/com/example/facturacion_inventario/data/remote/model/StockDto.kt`

**Contenido:**
- `StockByAlmacenDto` - Stock por almacén individual
- `StockResponseDto` - Respuesta de consulta GET con desglose
- `StockDto` - Detalles de stock individual
- `AdjustStockRequest` - Request para ajustar stock
- `SetStockRequest` - Request para establecer stock absoluto
- `StockOperationResponse` - Respuesta de operaciones POST
- `StockErrorResponse` - Manejo de errores

---

### 2. **Servicio Retrofit**
📁 `app/src/main/java/com/example/facturacion_inventario/data/remote/api/StockApiService.kt`

**Endpoints implementados:**

```kotlin
// ✅ GET /api/stock?productoId={id}
suspend fun obtenerStock(productoId: String): Response<StockResponseDto>

// 🔐 POST /api/stock/adjust
suspend fun ajustarStock(request: AdjustStockRequest): Response<StockOperationResponse>

// 🔐 POST /api/stock/set
suspend fun establecerStock(request: SetStockRequest): Response<StockOperationResponse>
```

---

### 3. **Repository con Manejo de Errores**
📁 `app/src/main/java/com/example/facturacion_inventario/data/repository/RemoteStockRepository.kt`

**Funcionalidades:**
- ✅ Obtención de stock con logging detallado
- ✅ Ajuste de stock (incremento/decremento)
- ✅ Establecimiento de stock absoluto
- ✅ Manejo de errores HTTP: 400, 403, 404, 409
- ✅ Excepción personalizada `StockException`
- ✅ Parseo de mensajes de error del backend

---

### 4. **ViewModel con StateFlow**
📁 `app/src/main/java/com/example/facturacion_inventario/ui/store/StockViewModel.kt`

**Estados:**
```kotlin
sealed class StockState {
    object Loading
    data class Success(total: Int, stockByAlmacen: List<StockByAlmacenDto>)
    data class Error(message: String)
}
```

**Funciones principales:**
- `loadStock(productoId)` - Carga stock del producto
- `adjustStock(...)` - Ajusta stock con callbacks
- `setStock(...)` - Establece stock absoluto
- `getStockLevel(total)` - Determina nivel de stock (OUT/LOW/IN)
- `hasStock()` - Valida disponibilidad
- `getTotalStock()` - Obtiene total actual

**Niveles de stock:**
```kotlin
enum class StockLevel {
    OUT_OF_STOCK,    // 0
    LOW_STOCK,       // 1-10
    IN_STOCK         // > 10
}
```

---

### 5. **Componentes Visuales Reutilizables**
📁 `app/src/main/java/com/example/facturacion_inventario/ui/components/stock/StockComponents.kt`

**Componentes:**

#### a) `StockBadge`
Badge visual con colores según disponibilidad:
- 🟢 **Verde**: Stock > 10 → "En stock (N)"
- 🟡 **Amarillo**: Stock 1-10 → "Pocas unidades (N)"
- 🔴 **Rojo**: Stock = 0 → "Sin stock"

```kotlin
StockBadge(total = 150, showIcon = true)
```

#### b) `StockIndicator`
Indicador compacto (solo icono y color):
```kotlin
StockIndicator(total = 5, modifier = Modifier.size(20.dp))
```

#### c) `StockDetailCard`
Card expandible con desglose por almacén:
```kotlin
StockDetailCard(stockState = stockState)
```

#### d) `StockLoadingSkeleton`
Skeleton screen durante carga:
```kotlin
StockLoadingSkeleton()
```

---

### 6. **Pantalla de Stock Detallado**
📁 `app/src/main/java/com/example/facturacion_inventario/ui/screens/ProductStockScreen.kt`

**Características:**
- ✅ Muestra stock total con badge visual
- ✅ Desglose detallado por almacén
- ✅ Botón de actualización (refresh)
- ✅ Estados: Loading, Success, Error
- ✅ Navegación con botón "Volver"

**Uso:**
```kotlin
ProductStockScreen(
    productId = "507f191e810c19729de860ea",
    productName = "Aceite de motor",
    onNavigateBack = { navController.popBackStack() }
)
```

---

### 7. **Integración en Pantalla de Detalle**
📁 `app/src/main/java/com/example/facturacion_inventario/ui/screens/ProductDetailScreenRemote.kt`

**Modificaciones:**
- ✅ Agrega `StockViewModel` como parámetro
- ✅ Carga stock en paralelo con producto
- ✅ Pasa `stockState` al contenido

📁 `app/src/main/java/com/example/facturacion_inventario/ui/screens/ProductDetailContentWithStock.kt`

**Nuevas funcionalidades:**
- ✅ Muestra stock en tiempo real desde backend
- ✅ Reemplaza stock estático del producto
- ✅ Valida stock antes de agregar al carrito
- ✅ Deshabilita botones si stock = 0
- ✅ Muestra card de desglose por almacén
- ✅ Skeleton durante carga de stock

---

### 8. **Configuración Retrofit**
📁 `app/src/main/java/com/example/facturacion_inventario/data/remote/api/RetrofitClient.kt`

**Modificación:**
```kotlin
val stockApiService: StockApiService by lazy {
    retrofit.create(StockApiService::class.java)
}
```

---

### 9. **Script Node.js de Ejemplo**
📁 `backend-config/actualizar_stock.js`

Script para probar la API desde Node.js con:
- ✅ Función `obtenerStock(productoId)`
- ✅ Función `ajustarStock(productoId, almacenId, delta)`
- ✅ Función `establecerStock(productoId, almacenId, cantidad)`
- ✅ Ejemplo de uso completo
- ✅ Manejo de errores

**Uso:**
```bash
cd backend-config
npm install axios
node actualizar_stock.js
```

---

## 🎯 Flujo de Usuario Implementado

### 1️⃣ Usuario ve lista de productos
→ Cada producto puede mostrar `StockIndicator` en la card

### 2️⃣ Usuario entra a detalle de producto
→ `ProductDetailScreenRemote` carga producto y stock en paralelo

### 3️⃣ Se muestra badge de stock
- 🟢 Verde si hay más de 10 unidades
- 🟡 Amarillo si quedan 1-10 unidades
- 🔴 Rojo si no hay stock

### 4️⃣ Usuario puede ver desglose
→ Card expandible muestra stock por almacén

### 5️⃣ Validación al agregar al carrito
- ✅ Botón habilitado solo si `stock > 0`
- ✅ Selector de cantidad limitado a stock disponible
- ❌ Botones deshabilitados si `stock = 0`

### 6️⃣ Actualización automática
→ El stock se recarga después de cada operación

---

## 🔧 Endpoints Utilizados

### ✅ GET /api/stock?productoId={id}
**Descripción:** Obtiene stock total con desglose por almacén  
**Autenticación:** No requerida  
**Uso:** Mostrar disponibilidad en UI

**Response 200:**
```json
{
  "stockByAlmacen": [
    {
      "almacenId": "507f1f77bcf86cd799439011",
      "almacenNombre": "Almacén Central",
      "cantidad": 50
    }
  ],
  "total": 150
}
```

---

### 🔐 POST /api/stock/adjust
**Descripción:** Ajusta stock (incrementa o decrementa)  
**Autenticación:** Requerida (JWT)  
**Uso:** Operaciones de inventario

**Request:**
```json
{
  "productoId": "507f191e810c19729de860ea",
  "almacenId": "507f1f77bcf86cd799439011",
  "delta": -30
}
```

**Response 200:**
```json
{
  "stock": {
    "productoId": "507f191e810c19729de860ea",
    "almacenId": "507f1f77bcf86cd799439011",
    "cantidad": 20,
    "actualizadoEn": "2024-10-30T10:30:00Z"
  },
  "total": 120
}
```

**Errores:**
- `400`: productoId y almacenId requeridos
- `403`: Permisos insuficientes
- `409`: Stock insuficiente en almacén

---

### 🔐 POST /api/stock/set
**Descripción:** Establece stock absoluto  
**Autenticación:** Requerida (JWT)  
**Uso:** Resetear stock o inventario físico

**Request:**
```json
{
  "productoId": "507f191e810c19729de860ea",
  "almacenId": "507f1f77bcf86cd799439011",
  "cantidad": 75
}
```

**Response 200:**
```json
{
  "stock": {
    "productoId": "507f191e810c19729de860ea",
    "almacenId": "507f1f77bcf86cd799439011",
    "cantidad": 75,
    "actualizadoEn": "2024-10-30T10:30:00Z"
  },
  "total": 175
}
```

---

## ✅ Checklist de Implementación

- [x] ✅ Crear DTOs (StockDto, StockResponseDto, AdjustStockRequest, SetStockRequest)
- [x] ✅ Crear StockApiService con los 3 endpoints
- [x] ✅ Crear RemoteStockRepository con manejo de errores
- [x] ✅ Crear StockViewModel con StateFlow
- [x] ✅ Crear componentes visuales (StockBadge, StockDetailCard, etc.)
- [x] ✅ Crear ProductStockScreen para mostrar desglose
- [x] ✅ Integrar en ProductDetailScreenRemote
- [x] ✅ Crear ProductDetailContentWithStock
- [x] ✅ Validar stock antes de "Agregar al carrito"
- [x] ✅ Deshabilitar botón si stock == 0
- [x] ✅ Agregar stockApiService a RetrofitClient
- [x] ✅ Crear script Node.js de ejemplo

---

## 🚀 Cómo Usar

### En ProductDetailScreen:
```kotlin
ProductDetailScreenRemote(
    productId = productId,
    cartViewModel = remoteCartViewModel,
    detailViewModel = viewModel(),
    stockViewModel = viewModel() // Nuevo parámetro
)
```

### Para mostrar solo el badge:
```kotlin
val stockViewModel: StockViewModel = viewModel()
val stockState by stockViewModel.stockState.collectAsState()

LaunchedEffect(productId) {
    stockViewModel.loadStock(productId)
}

when (val state = stockState) {
    is StockState.Success -> StockBadge(total = state.total)
    is StockState.Loading -> StockLoadingSkeleton()
    is StockState.Error -> Text("Error: ${state.message}")
}
```

### Para operaciones de inventario:
```kotlin
stockViewModel.adjustStock(
    productoId = "xxx",
    almacenId = "yyy",
    delta = -10,
    onSuccess = { newTotal -> 
        println("Stock actualizado: $newTotal") 
    },
    onError = { error -> 
        println("Error: $error") 
    }
)
```

---

## 🧪 Testing

### URL de prueba (Emulador Android):
```
GET http://10.0.2.2:8080/api/stock?productoId=507f191e810c19729de860ea
```

### URL de prueba (Dispositivo físico):
```
GET http://192.168.1.X:8080/api/stock?productoId=507f191e810c19729de860ea
```
*(Reemplazar X con IP de tu máquina)*

---

## 📱 Capturas de Flujo

### Badge de Stock:
- **En stock (150)** → Fondo verde claro, texto verde oscuro
- **Pocas unidades (5)** → Fondo naranja claro, texto naranja oscuro
- **Sin stock** → Fondo rojo claro, texto rojo oscuro

### Card de Desglose:
```
┌─────────────────────────────────┐
│ Disponibilidad   [En stock (150)]│
├─────────────────────────────────┤
│ Stock por almacén:              │
│  📦 Almacén Central: 50 unidades│
│  📦 Almacén Norte: 100 unidades │
└─────────────────────────────────┘
```

---

## 🔍 Logging

Todos los componentes incluyen logging detallado con tags:
- `RemoteStockRepo` - Operaciones de red
- `StockViewModel` - Lógica de negocio

Ejemplo de logs:
```
D/RemoteStockRepo: 🔍 Fetching stock for producto: 507f191e810c19729de860ea
D/RemoteStockRepo: 📡 Response code: 200
D/RemoteStockRepo: ✅ Stock total: 150
D/RemoteStockRepo:   📦 Almacén Central: 50 unidades
D/RemoteStockRepo:   📦 Almacén Norte: 100 unidades
```

---

## 🎨 Personalización

### Cambiar umbrales de stock:
En `StockViewModel.getStockLevel()`:
```kotlin
return when {
    total == 0 -> StockLevel.OUT_OF_STOCK
    total <= 5 -> StockLevel.LOW_STOCK  // Cambiado de 10 a 5
    else -> StockLevel.IN_STOCK
}
```

### Cambiar colores del badge:
En `StockComponents.kt`, modificar los colores en `StockBadge`:
```kotlin
Color(0xFF4CAF50) // Verde - En stock
Color(0xFFFF9800) // Naranja - Pocas unidades
Color(0xFFF44336) // Rojo - Sin stock
```

---

## ⚠️ Notas Importantes

1. **Backend debe estar corriendo** en `http://10.0.2.2:8080` (emulador)
2. **Endpoints de modificación** (`adjust`, `set`) requieren autenticación JWT
3. **RetrofitClient debe estar inicializado** con contexto antes de usar
4. **Validar IDs** - Los IDs de ejemplo deben reemplazarse con IDs reales de MongoDB

---

## 🐛 Troubleshooting

### Error 401 Unauthorized
→ Verificar que `RetrofitClient.initialize(context)` se llama en `Application`

### Error "Connection refused"
→ Backend no está corriendo o URL incorrecta

### Stock no se actualiza
→ Verificar que `LaunchedEffect(productId)` está presente

### Botones deshabilitados incorrectamente
→ Verificar que `stockState is StockState.Success` antes de validar

---

## 📚 Referencias

- Documentación Retrofit: https://square.github.io/retrofit/
- StateFlow Guide: https://developer.android.com/kotlin/flow/stateflow-and-sharedflow
- Compose State: https://developer.android.com/jetpack/compose/state

---

**Implementación completada exitosamente** ✅

_Última actualización: 2025-11-09_

