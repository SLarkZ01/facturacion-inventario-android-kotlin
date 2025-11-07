# Consumo de API REST - Productos

## 📋 Resumen

Se ha implementado la integración completa con la API REST de productos del backend Spring Boot en Android Kotlin usando:
- **Retrofit** para las llamadas HTTP
- **Coroutines** para operaciones asíncronas
- **StateFlow** para manejo de estados reactivo
- **MVVM** (Model-View-ViewModel) como arquitectura

## 🗂️ Estructura de Archivos Creados

### 1. Modelos de Datos (DTOs)
📁 `data/remote/model/ProductoDto.kt`
- `ProductoDto`: Coincide exactamente con el modelo de MongoDB del backend
- `MedioDto`: Para los elementos multimedia (imágenes/videos)
- `ProductosResponse`: Wrapper para lista de productos
- `ProductoResponse`: Wrapper para un solo producto
- `ProductoRequest`: Para crear/actualizar productos

### 2. API Service
📁 `data/remote/api/ProductoApiService.kt`
Define todos los endpoints disponibles:
- `GET /api/productos` - Listar con filtros (categoría, búsqueda)
- `GET /api/productos/{id}` - Obtener uno
- `POST /api/productos` - Crear
- `PUT /api/productos/{id}` - Actualizar
- `PATCH /api/productos/{id}/stock` - Ajustar stock
- `DELETE /api/productos/{id}` - Eliminar

### 3. Cliente Retrofit
📁 `data/remote/api/RetrofitClient.kt`
- Configuración singleton de Retrofit
- Usa la misma BASE_URL que autenticación (`http://10.0.2.2:8080/`)
- Logging interceptor para debug
- Timeouts configurados (30s)

### 4. Mapper
📁 `data/remote/mapper/ProductoMapper.kt`
- Convierte `ProductoDto` (API) → `Product` (dominio)
- Mapea tipos de medios correctamente

### 5. Repositorio
📁 `data/repository/RemoteProductRepository.kt`
- Implementa `ProductRepository`
- Métodos asíncronos con `Result<T>`
- `getProductsAsync()` - todos o filtrados
- `getProductByIdAsync()` - por ID
- `searchProducts()` - búsqueda por nombre
- `getProductsByCategory()` - por categoría

### 6. ViewModels
📁 `ui/store/ProductViewModel.kt`
- `ProductListViewModel`: Para listas de productos
  - Estados: Loading, Success, Error, Empty
  - Métodos: loadProducts(), searchProducts(), filterByCategory()
- `ProductDetailViewModel`: Para detalle de producto
  - Estados: Loading, Success, Error
  - Métodos: loadProduct(), retry()

### 7. Pantallas UI
📁 `ui/screens/HomeScreenRemote.kt`
- `HomeScreenRemote`: Lista de productos desde API
- Maneja todos los estados (carga, éxito, error, vacío)
- `HomeScreenHybrid`: Permite cambiar entre datos fake y reales

📁 `ui/screens/ProductDetailScreenRemote.kt`
- `ProductDetailScreenRemote`: Detalle de producto desde API
- Integrado con el `CartViewModel` existente

## 🚀 Cómo Usar

### Opción 1: Cambiar a datos reales en toda la app

Edita `StoreScreens.kt` y reemplaza:

```kotlin
@Composable
fun HomeScreen(navController: NavController, selectedCategoryId: String? = null) {
    HomeScreenRemote(
        onProductClick = { id -> navController.navigate(Routes.productRoute(id)) },
        categoryId = selectedCategoryId
    )
}

@Composable
fun ProductDetailScreen(productId: String?, cartViewModel: CartViewModel = viewModel()) {
    productId?.let {
        ProductDetailScreenRemote(
            productId = it,
            cartViewModel = cartViewModel
        )
    }
}
```

### Opción 2: Usar modo híbrido (toggle entre fake y real)

```kotlin
var useRemoteData by remember { mutableStateOf(false) }

HomeScreenHybrid(
    useRemoteData = useRemoteData,
    onProductClick = { id -> navController.navigate(Routes.productRoute(id)) }
)
```

### Opción 3: Usar el ViewModel directamente en cualquier pantalla

```kotlin
@Composable
fun MiPantalla() {
    val viewModel: ProductListViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }
    
    when (state) {
        is ProductListState.Loading -> { /* Mostrar loading */ }
        is ProductListState.Success -> { /* Mostrar lista */ }
        is ProductListState.Error -> { /* Mostrar error */ }
        is ProductListState.Empty -> { /* Mostrar mensaje vacío */ }
    }
}
```

## 🔧 Configuración del Backend

Asegúrate que tu backend Spring Boot esté corriendo en:
- **Local**: `http://localhost:8080`
- **Emulador Android**: `http://10.0.2.2:8080` (ya configurado)
- **Dispositivo físico**: Usa la IP de tu computadora en la red local

Para cambiar la URL, edita:
```kotlin
// data/auth/ApiConfig.kt
const val BASE_URL = "http://tu-ip:8080/"
```

## 📊 Estructura de Datos del Backend

Tu backend devuelve:

```json
{
  "productos": [
    {
      "id": "507f1f77bcf86cd799439011",
      "idString": "PROD-001",
      "nombre": "Pistón 150cc",
      "descripcion": "Pistón completo con anillos",
      "precio": 45000.0,
      "stock": 12,
      "categoriaId": "motor",
      "imagenRecurso": 2131165318,
      "listaMedios": [
        {
          "idRecurso": 2131165318,
          "tipo": "IMAGE"
        }
      ],
      "creadoEn": "2025-01-06T10:30:00.000+0000"
    }
  ]
}
```

## ⚠️ Notas Importantes

### Para Cliente (visualización)
Si solo quieres **mostrar productos** en la app (GET):
- Usa `ProductListViewModel` y `ProductDetailViewModel`
- Solo implementa las pantallas de lectura
- No necesitas los endpoints POST/PUT/DELETE

### Para Administrador (CRUD completo)
Si quieres **crear/editar/eliminar** productos:
- Los endpoints ya están en `ProductoApiService`
- Necesitarás crear pantallas de formulario
- Implementar validaciones
- Manejar permisos/autenticación

### Backend sin productos
El endpoint `GET /api/productos` devuelve lista vacía si no hay filtros.
**Solución**: Siempre usar `query` o `categoriaId`:
```kotlin
viewModel.loadProducts(query = "") // Buscar con query vacío
viewModel.loadProducts(categoryId = "motor") // Por categoría
```

O modificar el backend para devolver todos por defecto.

## 🧪 Testing

### Probar conexión con el backend:
1. Inicia tu backend Spring Boot
2. Verifica que responda: `http://localhost:8080/api/productos?q=`
3. Ejecuta la app Android en emulador
4. Observa los logs en Logcat con filtro `ProductListViewModel`

### Logs importantes:
```
D/ProductListViewModel: Loaded 10 products successfully
D/RemoteProductRepo: Fetching products - categoriaId: motor, query: null
D/RemoteProductRepo: Successfully fetched 5 products
```

### Errores comunes:
- **Connection refused**: Backend no está corriendo
- **404 Not Found**: URL incorrecta o endpoint no existe
- **Empty list**: Backend no tiene productos o falta query parameter
- **Timeout**: Backend demasiado lento o red inestable

## 🎯 Próximos Pasos (Opcionales)

1. **Caché local con Room**: Guardar productos offline
2. **Paginación**: Usar Paging 3 para listas grandes
3. **Búsqueda en tiempo real**: Debounce con Flow
4. **Imágenes remotas**: Cargar desde URLs con Coil
5. **Autenticación**: Agregar token Bearer en headers
6. **Formularios CRUD**: Pantallas para crear/editar productos
7. **Pull-to-refresh**: Swipe para recargar
8. **Filtros avanzados**: Por precio, stock, etc.

## 📱 Dependencias Necesarias (Ya incluidas)

```kotlin
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
```

## 🌐 Permiso en AndroidManifest.xml

Ya deberías tenerlo, pero verifica:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

**¡Listo para usar!** 🎉

Para empezar, simplemente reemplaza `HomeScreen` y `ProductDetailScreen` en `StoreScreens.kt` con las versiones Remote.

