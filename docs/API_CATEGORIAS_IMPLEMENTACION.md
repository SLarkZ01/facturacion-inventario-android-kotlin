# Implementación de la API de Categorías

## 📋 Resumen

Se ha implementado el consumo completo de la API de categorías desde el backend de Spring Boot.

## 🔗 Endpoints Implementados

### Base URL: `/api/categorias`

#### 1. Listar Categorías
```
GET /api/categorias
```
**Parámetros opcionales:**
- `query` (String): Búsqueda por nombre
- `page` (Int): Número de página (default: 0)
- `size` (Int): Tamaño de página (default: 20)
- `tallerId` (String): Filtro por taller
- `global` (Boolean): Si true, solo categorías globales

**Respuesta:**
```json
{
  "categorias": [
    {
      "id": "507f1f77bcf86cd799439011",
      "idString": "categoria-1",
      "nombre": "Motor",
      "descripcion": "Funcionamiento interno de la moto",
      "iconoRecurso": null,
      "tallerId": null,
      "mappedGlobalCategoryId": null,
      "creadoEn": "2025-01-07T10:30:00.000Z"
    }
  ],
  "totalElements": 10,
  "totalPages": 1,
  "currentPage": 0
}
```

#### 2. Obtener Categoría por ID
```
GET /api/categorias/{id}
```

**Respuesta:**
```json
{
  "categoria": {
    "id": "507f1f77bcf86cd799439011",
    "nombre": "Motor",
    "descripcion": "Funcionamiento interno de la moto",
    ...
  }
}
```

## 📦 Estructura de Archivos Implementados

### 1. **CategoriaDto.kt** - Modelo de Datos
```kotlin
data class CategoriaDto(
    val id: String,
    val idString: String?,
    val nombre: String,
    val descripcion: String?,
    val iconoRecurso: Int?,
    val tallerId: String?,
    val mappedGlobalCategoryId: String?,
    val creadoEn: Date?
)
```

### 2. **CategoriaMapper.kt** - Convertidor
Convierte `CategoriaDto` (API) → `Category` (dominio)
- Maneja iconos inteligentemente
- Si el backend envía `iconoRecurso`, lo usa
- Si no, busca por nombre (motor, frenos, etc.)
- Icono por defecto si no encuentra coincidencia

### 3. **CategoriaApiService.kt** - Servicio Retrofit
Interface con los métodos suspend para llamadas asíncronas:
- `listarCategorias()` - Lista con filtros y paginación
- `obtenerCategoria(id)` - Obtiene una categoría específica

### 4. **RetrofitClient.kt** - Cliente HTTP
- Configurado con timeouts de 30 segundos
- Logging completo para debugging
- Manejo de fechas en formato ISO 8601

### 5. **RemoteCategoryRepository.kt** - Repositorio
Ya existía y funciona con el nuevo servicio:
- `getCategoriesAsync()` - Carga categorías con filtros
- `getCategoryByIdAsync(id)` - Carga una categoría
- `searchCategories(query)` - Búsqueda por nombre
- Manejo robusto de errores con Result<T>

### 6. **CategoryViewModel.kt** - ViewModel
- **Fallback automático**: Si la API falla, carga datos locales
- **Manejo de errores**: Try-catch en todos los métodos
- **Estados claros**: Loading, Success, Error, Empty
- **Sin crashes**: La app nunca se cierra por errores de red

## 🚀 Cómo Funciona

1. **Usuario entra a Categorías** → `CategoriesScreen` se muestra
2. **ViewModel se inicializa** → Llama a `loadCategories()`
3. **Intenta API remota** → `RemoteCategoryRepository.getCategoriesAsync()`
4. **Si la API responde** → Muestra categorías del backend
5. **Si la API falla** → Automáticamente carga `FakeCategoryRepository` (10 categorías locales)
6. **Resultado** → Usuario siempre ve categorías, sin crashes

## 🔧 Mapeo del Modelo Backend

### Backend (Java/Spring Boot)
```java
@Document(collection = "categorias")
public class Categoria {
    private String id;                      // MongoDB ObjectId
    private String idString;                // ID alternativo
    private String nombre;
    private String descripcion;
    private Integer iconoRecurso;
    private String tallerId;                // null = global
    private String mappedGlobalCategoryId;
    private Date creadoEn;
}
```

### Android (Kotlin)
```kotlin
data class CategoriaDto(
    val id: String,
    val idString: String?,
    val nombre: String,
    val descripcion: String?,
    val iconoRecurso: Int?,
    val tallerId: String?,
    val mappedGlobalCategoryId: String?,
    val creadoEn: Date?
)
```

### Modelo de Dominio
```kotlin
data class Category(
    val id: String,
    val name: String,
    val description: String,
    val iconRes: Int
)
```

## ✅ Ventajas de esta Implementación

1. **Robusta**: Manejo completo de errores
2. **Fallback**: Siempre hay datos disponibles
3. **Sin crashes**: La app nunca se cierra
4. **Flexible**: Soporta filtros por taller, búsqueda, paginación
5. **Bien tipada**: Uso de sealed classes para estados
6. **Logging completo**: Fácil de debuggear
7. **Separación de capas**: DTO → Mapper → Domain → UI

## 🧪 Pruebas

Para probar, simplemente:
1. Asegúrate de que tu backend esté corriendo
2. Verifica la URL en `ApiConfig.BASE_URL`
3. Entra a la pantalla de Categorías
4. Revisa los logs en Logcat filtrando por `CategoryViewModel`

Si la API no está disponible, verás automáticamente las 10 categorías locales.

## 🔍 Debugging

Ver logs detallados en Logcat:
```
adb logcat | findstr "CategoryViewModel RemoteCategoryRepo"
```

Logs típicos:
- `🔍 Loading categories...` - Iniciando carga
- `📡 API Call - Fetching categories...` - Llamada a API
- `✅ SUCCESS: Loaded X categories` - Éxito
- `❌ ERROR loading categories` - Error de API
- `📦 Loading fallback categories...` - Cargando locales

