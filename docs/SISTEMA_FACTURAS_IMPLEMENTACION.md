# 📄 Sistema de Facturas - Documentación Completa

## ✅ Archivos Implementados

### 1. Modelos de Datos (DTOs)
- `FacturaDto.kt` - DTOs para comunicación con el backend
- `Factura.kt` - Modelos de dominio

### 2. Capa de Datos
- `FacturaApiService.kt` - Interface Retrofit para endpoints
- `FacturaMapper.kt` - Conversión DTO ↔ Domain
- `RemoteFacturaRepository.kt` - Repositorio con lógica de negocio
- `RetrofitClient.kt` - ✅ Actualizado con `facturaApiService`

### 3. Capa de Presentación
- `FacturaViewModel.kt` - ViewModel con estados reactivos
- `CheckoutScreen.kt` - Pantalla de confirmación de compra
- `FacturasScreen.kt` - Listado de facturas del usuario
- `FacturaDetalleScreen.kt` - Detalle completo de una factura

---

## 🚀 Guía de Integración

### Paso 1: Verificar que RetrofitClient esté inicializado

En tu `MainActivity` o `Application`:

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar RetrofitClient (ya debería estar)
        RetrofitClient.initialize(this)
        
        setContent {
            // Tu composable principal
        }
    }
}
```

### Paso 2: Agregar las rutas de navegación

En tu archivo de navegación (por ejemplo, `Navigation.kt` o donde definas tu `NavHost`):

```kotlin
sealed class Screen(val route: String) {
    object Checkout : Screen("checkout/{carritoId}")
    object Facturas : Screen("facturas")
    object FacturaDetalle : Screen("factura/{facturaId}")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        
        // ... tus rutas existentes ...
        
        // Ruta de Checkout
        composable(
            route = "checkout/{carritoId}",
            arguments = listOf(navArgument("carritoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val carritoId = backStackEntry.arguments?.getString("carritoId") ?: ""
            
            // Necesitas obtener los items del carrito
            // Puedes usar tu CarritoViewModel existente
            val carritoViewModel: CarritoViewModel = viewModel()
            val cartItems by carritoViewModel.cartItems.collectAsState()
            
            CheckoutScreen(
                carritoId = carritoId,
                cartItems = cartItems,
                onNavigateBack = { navController.popBackStack() },
                onCheckoutSuccess = { facturaId ->
                    // Navegar al detalle de la factura creada
                    navController.navigate("factura/$facturaId") {
                        popUpTo("checkout/$carritoId") { inclusive = true }
                    }
                }
            )
        }
        
        // Ruta de Listado de Facturas
        composable("facturas") {
            val userId = obtenerUserId() // Tu método para obtener el userId actual
            
            FacturasScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() },
                onFacturaClick = { facturaId ->
                    navController.navigate("factura/$facturaId")
                }
            )
        }
        
        // Ruta de Detalle de Factura
        composable(
            route = "factura/{facturaId}",
            arguments = listOf(navArgument("facturaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val facturaId = backStackEntry.arguments?.getString("facturaId") ?: ""
            
            FacturaDetalleScreen(
                facturaId = facturaId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
```

### Paso 3: Integrar el botón de checkout en tu pantalla de carrito

En tu pantalla existente del carrito:

```kotlin
@Composable
fun CartScreen(
    carritoId: String,
    cartItems: List<CartItem>,
    navController: NavController
) {
    Column {
        // ... tu UI existente del carrito ...
        
        // Botón para ir al checkout
        Button(
            onClick = {
                if (cartItems.isNotEmpty()) {
                    navController.navigate("checkout/$carritoId")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = cartItems.isNotEmpty()
        ) {
            Icon(Icons.Default.ShoppingCart, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Proceder al Pago")
        }
    }
}
```

### Paso 4: Agregar acceso al historial de facturas

En tu menú principal, drawer o bottom navigation:

```kotlin
NavigationDrawerItem(
    icon = { Icon(Icons.Default.Receipt, contentDescription = null) },
    label = { Text("Mis Facturas") },
    selected = false,
    onClick = {
        navController.navigate("facturas")
        scope.launch { drawerState.close() }
    }
)
```

---

## 📱 Flujo Completo de Usuario

1. **Usuario ve su carrito** → GET `/api/carritos/{id}`
   ```kotlin
   carritoViewModel.cargarCarrito(carritoId)
   ```

2. **Usuario presiona "Proceder al Pago"** → Navega a `CheckoutScreen`
   ```kotlin
   navController.navigate("checkout/$carritoId")
   ```

3. **Usuario revisa el resumen y confirma** → POST `/api/facturas/checkout`
   ```kotlin
   facturaViewModel.realizarCheckout(carritoId)
   ```

4. **Backend procesa:**
   - ✅ Valida stock disponible
   - ✅ Crea factura
   - ✅ Actualiza stock (resta cantidades)
   - ✅ Devuelve factura creada

5. **App muestra confirmación** → Navega a `FacturaDetalleScreen`
   ```kotlin
   navController.navigate("factura/$facturaId")
   ```

6. **Usuario puede ver historial** → GET `/api/facturas?userId={id}`
   ```kotlin
   navController.navigate("facturas")
   ```

---

## 🔧 Uso del ViewModel (ejemplos directos)

### Realizar Checkout

```kotlin
val facturaViewModel: FacturaViewModel = viewModel()
val checkoutState by facturaViewModel.checkoutState.collectAsState()

// Iniciar checkout
facturaViewModel.realizarCheckout(carritoId)

// Observar estado
when (checkoutState) {
    is FacturaViewModel.CheckoutState.Idle -> { /* Estado inicial */ }
    is FacturaViewModel.CheckoutState.Loading -> { /* Mostrar loading */ }
    is FacturaViewModel.CheckoutState.Success -> {
        val factura = (checkoutState as FacturaViewModel.CheckoutState.Success).factura
        // Navegar o mostrar éxito
    }
    is FacturaViewModel.CheckoutState.Error -> {
        val mensaje = (checkoutState as FacturaViewModel.CheckoutState.Error).message
        // Mostrar error
    }
}
```

### Listar Facturas

```kotlin
val facturaViewModel: FacturaViewModel = viewModel()
val facturasState by facturaViewModel.facturasState.collectAsState()

// Cargar facturas
LaunchedEffect(userId) {
    facturaViewModel.cargarFacturas(userId)
}

// Observar estado
when (facturasState) {
    is FacturaViewModel.FacturasState.Loading -> { /* Mostrar loading */ }
    is FacturaViewModel.FacturasState.Empty -> { /* Sin facturas */ }
    is FacturaViewModel.FacturasState.Success -> {
        val facturas = (facturasState as FacturaViewModel.FacturasState.Success).facturas
        // Mostrar lista
    }
    is FacturaViewModel.FacturasState.Error -> { /* Mostrar error */ }
}
```

### Ver Detalle de Factura

```kotlin
val facturaViewModel: FacturaViewModel = viewModel()
val detalleState by facturaViewModel.facturaDetalleState.collectAsState()

// Cargar detalle
LaunchedEffect(facturaId) {
    facturaViewModel.cargarDetalleFactura(facturaId)
}

// Observar estado
when (detalleState) {
    is FacturaViewModel.FacturaDetalleState.Loading -> { /* Loading */ }
    is FacturaViewModel.FacturaDetalleState.Success -> {
        val factura = (detalleState as FacturaViewModel.FacturaDetalleState.Success).factura
        // Mostrar detalle
    }
    is FacturaViewModel.FacturaDetalleState.Error -> { /* Error */ }
}
```

---

## 🔐 Autenticación JWT

El sistema ya está configurado para usar autenticación JWT automáticamente gracias al `authInterceptor` en `RetrofitClient`.

**Requisitos:**
- El token JWT debe estar guardado usando `TokenStorage.getAccessToken(context)`
- El endpoint `/api/facturas/checkout` REQUIERE autenticación
- Si el token no es válido, recibirás error 401

**Manejo de errores 401:**

```kotlin
when (checkoutState) {
    is FacturaViewModel.CheckoutState.Error -> {
        val mensaje = (checkoutState as FacturaViewModel.CheckoutState.Error).message
        if (mensaje.contains("401") || mensaje.contains("autenticado")) {
            // Redirigir al login
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}
```

---

## 🎨 Personalización de UI

Las pantallas están diseñadas con Material 3 y son completamente personalizables:

### Cambiar colores

Las pantallas usan `MaterialTheme.colorScheme`, así que los colores se adaptan automáticamente a tu tema.

### Agregar más información

Puedes extender los modelos y DTOs si el backend envía más campos:

```kotlin
// En FacturaDto.kt
data class FacturaDto(
    // ... campos existentes ...
    @SerializedName("metodoPago")
    val metodoPago: String? = null,
    
    @SerializedName("observaciones")
    val observaciones: String? = null
)

// En Factura.kt
data class Factura(
    // ... campos existentes ...
    val metodoPago: String? = null,
    val observaciones: String? = null
)

// Actualizar FacturaMapper.kt
fun toDomain(dto: FacturaDto): Factura {
    return Factura(
        // ... campos existentes ...
        metodoPago = dto.metodoPago,
        observaciones = dto.observaciones
    )
}
```

---

## 🐛 Manejo de Errores

### Errores comunes y soluciones

| Error | Causa | Solución |
|-------|-------|----------|
| **401: No autenticado** | Token JWT inválido o expirado | Re-iniciar sesión |
| **400: Carrito inválido** | Carrito vacío o no existe | Validar antes de checkout |
| **409: Stock insuficiente** | No hay suficiente inventario | Mostrar mensaje al usuario |
| **404: Factura no encontrada** | ID incorrecto | Verificar ID |
| **Network error** | Sin conexión o backend caído | Mostrar mensaje de reintento |

### Ejemplo de manejo robusto

```kotlin
fun realizarCheckoutConManejo(
    carritoId: String,
    viewModel: FacturaViewModel,
    navController: NavController,
    onError: (String) -> Unit
) {
    viewModel.realizarCheckout(carritoId)
    
    viewModel.checkoutState.collect { state ->
        when (state) {
            is FacturaViewModel.CheckoutState.Success -> {
                navController.navigate("factura/${state.factura.id}")
            }
            is FacturaViewModel.CheckoutState.Error -> {
                when {
                    state.message.contains("401") -> {
                        onError("Sesión expirada. Por favor inicia sesión nuevamente.")
                        navController.navigate("login")
                    }
                    state.message.contains("409") -> {
                        onError("Stock insuficiente para completar la compra.")
                    }
                    else -> {
                        onError(state.message)
                    }
                }
            }
        }
    }
}
```

---

## 📊 Modelos de Respuesta del Backend

### Checkout exitoso

```json
{
  "factura": {
    "id": "507f1f77bcf86cd799439888",
    "numeroFactura": "FAC-2024-001",
    "clienteId": "507f1f77bcf86cd799439011",
    "items": [
      {
        "productoId": "507f191e810c19729de860ea",
        "cantidad": 5,
        "precioUnitario": 25.50
      }
    ],
    "total": 127.50,
    "estado": "PAGADA",
    "creadoEn": "2024-10-30T10:30:00Z"
  }
}
```

### Listado de facturas

```json
{
  "facturas": [
    {
      "id": "507f1f77bcf86cd799439888",
      "numeroFactura": "FAC-2024-001",
      "total": 127.50,
      "estado": "PAGADA",
      "creadoEn": "2024-10-30T10:30:00Z"
    }
  ]
}
```

---

## ✅ Checklist de Implementación

- [x] ✅ Modelos de datos creados (DTO y Domain)
- [x] ✅ Mapper implementado
- [x] ✅ API Service creado
- [x] ✅ RetrofitClient actualizado
- [x] ✅ Repository implementado
- [x] ✅ ViewModel con estados reactivos
- [x] ✅ Pantallas de UI (Checkout, Listado, Detalle)
- [ ] ⏳ Agregar rutas de navegación (ver Paso 2)
- [ ] ⏳ Integrar botón de checkout en carrito (ver Paso 3)
- [ ] ⏳ Agregar opción "Mis Facturas" en menú (ver Paso 4)
- [ ] ⏳ Probar flujo completo

---

## 🧪 Testing

### Probar con backend local

1. Verifica que tu backend esté corriendo en `http://10.0.2.2:8080` (emulador) o `http://tu-ip:8080` (dispositivo físico)

2. Verifica la configuración en `ApiConfig`:
```kotlin
object ApiConfig {
    const val BASE_URL = "http://10.0.2.2:8080/" // Para emulador
    // const val BASE_URL = "http://192.168.x.x:8080/" // Para dispositivo físico
}
```

3. Usa Logcat para ver los logs del Repository:
```
TAG: RemoteFacturaRepo
- 🔍 Buscar por "🛒 Iniciando checkout"
- ✅ Buscar por "✅ Factura creada exitosamente"
- ❌ Buscar por "❌" para errores
```

---

## 📞 Soporte

Si encuentras problemas:
1. Revisa los logs en Logcat (filtrar por "RemoteFacturaRepo")
2. Verifica que el backend esté corriendo
3. Confirma que tienes un token JWT válido
4. Verifica que el carrito tenga items antes del checkout

---

**¡Sistema de facturas listo para usar! 🎉**

