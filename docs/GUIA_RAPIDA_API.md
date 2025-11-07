# ✅ GUÍA RÁPIDA - Consumo de API de Productos

## 🎯 ¿Qué se implementó?

Se creó toda la arquitectura para consumir la API REST de productos del backend Spring Boot:

### ✅ Archivos Creados:

1. **📦 Modelos** (`data/remote/model/ProductoDto.kt`)
   - DTOs que coinciden con tu backend MongoDB

2. **🌐 API Service** (`data/remote/api/ProductoApiService.kt`)
   - Todos los endpoints: GET, POST, PUT, PATCH, DELETE

3. **🔧 Retrofit Client** (`data/remote/api/RetrofitClient.kt`)
   - Configurado con logging y timeouts

4. **🔄 Mapper** (`data/remote/mapper/ProductoMapper.kt`)
   - Convierte DTOs a modelos de dominio

5. **💾 Repositorio** (`data/repository/RemoteProductRepository.kt`)
   - Lógica de negocio con manejo de errores

6. **🎨 ViewModels** (`ui/store/ProductViewModel.kt`)
   - ProductListViewModel y ProductDetailViewModel
   - Estados: Loading, Success, Error, Empty

7. **📱 Pantallas UI** (`ui/screens/`)
   - HomeScreenRemote: Lista de productos
   - ProductDetailScreenRemote: Detalle de producto

8. **📚 Documentación** (`docs/CONSUMO_API_PRODUCTOS.md`)
   - Guía completa con todos los detalles

---

## 🚀 CÓMO USAR (3 pasos)

### Paso 1: Inicia tu Backend Spring Boot
```bash
# En tu proyecto backend, ejecuta:
./mvnw spring-boot:run
# O
./gradlew bootRun
```

Verifica que responda en: `http://localhost:8080/api/productos?q=`

### Paso 2: Edita `StoreScreens.kt`

**Opción A - Reemplazo Simple (Recomendado):**

Abre `app/src/main/java/com/example/facturacion_inventario/ui/store/StoreScreens.kt`

**REEMPLAZA** la función `HomeScreen`:
```kotlin
@Composable
fun HomeScreen(navController: NavController, selectedCategoryId: String? = null) {
    HomeScreenRemote(
        onProductClick = { id -> navController.navigate(Routes.productRoute(id)) },
        categoryId = selectedCategoryId
    )
}
```

**REEMPLAZA** la función `ProductDetailScreen`:
```kotlin
@Composable
fun ProductDetailScreen(productId: String?, cartViewModel: CartViewModel = viewModel()) {
    if (productId != null) {
        ProductDetailScreenRemote(
            productId = productId,
            cartViewModel = cartViewModel
        )
    }
}
```

**Opción B - Copiar desde el ejemplo:**

Copia las funciones del archivo: `ui/store/IntegracionAPIEjemplo.kt`

### Paso 3: Ejecuta la App
```
Run > Run 'app'
```

---

## ⚠️ IMPORTANTE: Backend sin datos

Tu backend Spring Boot tiene un comportamiento especial:
- Si haces `GET /api/productos` sin parámetros → devuelve lista vacía
- Necesitas usar `?q=` o `?categoriaId=`

**Soluciones:**

1. **Modifica el backend** para que devuelva todos los productos por defecto
2. **O usa búsqueda vacía** en el ViewModel (ya implementado):
   ```kotlin
   viewModel.loadProducts(query = "")
   ```

---

## 🎛️ Configuración de URL

Si tu backend NO está en `localhost:8080`, edita:

`app/src/main/java/com/example/data/auth/ApiConfig.kt`
```kotlin
const val BASE_URL = "http://TU_IP:8080/"
```

**Para emulador Android:** `http://10.0.2.2:8080/` (ya configurado)
**Para dispositivo físico:** `http://192.168.X.X:8080/` (IP de tu PC)

---

## 📊 Para SOLO visualizar productos (Cliente)

Si tu app es **solo para clientes** (ver productos, agregar al carrito):
- ✅ Ya tienes todo listo con GET `/api/productos` y GET `/api/productos/{id}`
- ✅ Usa `ProductListViewModel` y `ProductDetailViewModel`
- ❌ NO necesitas implementar POST/PUT/DELETE

## 🛠️ Para gestionar productos (Admin)

Si quieres **crear/editar/eliminar** productos desde la app:
- ✅ Los endpoints ya están en `ProductoApiService`
- ⚠️ Necesitarás crear pantallas de formulario
- ⚠️ Agregar validaciones y manejo de permisos

---

## 🐛 Troubleshooting

### Error: "Connection refused"
- ✅ Verifica que el backend esté corriendo
- ✅ Usa `http://10.0.2.2:8080/` en emulador

### Error: "404 Not Found"
- ✅ Verifica la URL en `ApiConfig.kt`
- ✅ Confirma que el endpoint existe en tu backend

### Lista vacía pero backend tiene datos
- ✅ El backend requiere query parameter
- ✅ Modifica backend o usa `query = ""`

### Logs para debug
Filtra Logcat por:
- `ProductListViewModel`
- `RemoteProductRepo`
- `OkHttp`

---

## 📁 Estructura Final

```
app/src/main/java/com/example/facturacion_inventario/
├── data/
│   ├── remote/
│   │   ├── api/
│   │   │   ├── ProductoApiService.kt ✅
│   │   │   └── RetrofitClient.kt ✅
│   │   ├── mapper/
│   │   │   └── ProductoMapper.kt ✅
│   │   └── model/
│   │       └── ProductoDto.kt ✅
│   └── repository/
│       ├── FakeProductRepository.kt (viejo)
│       └── RemoteProductRepository.kt ✅ NUEVO
├── ui/
│   ├── screens/
│   │   ├── HomeScreenRemote.kt ✅ NUEVO
│   │   └── ProductDetailScreenRemote.kt ✅ NUEVO
│   └── store/
│       ├── ProductViewModel.kt ✅ NUEVO
│       └── StoreScreens.kt (editar aquí)
└── docs/
    └── CONSUMO_API_PRODUCTOS.md ✅ Guía completa
```

---

## ✨ ¡Listo para usar!

Ya tienes todo implementado. Solo necesitas:
1. ✅ Iniciar el backend
2. ✅ Editar `StoreScreens.kt` (copiar 2 funciones)
3. ✅ Ejecutar la app

**¿Preguntas?** Lee `docs/CONSUMO_API_PRODUCTOS.md` para más detalles.

