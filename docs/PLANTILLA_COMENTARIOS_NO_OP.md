# Plantilla de Documentación para Funciones No-Op / Placeholder

## 📋 Plantilla Estándar

Usar esta plantilla para documentar funciones vacías, no-op o placeholder:

```kotlin
/**
 * ⚠️ FUNCIÓN NO-OP / PLACEHOLDER
 * 
 * PROPÓSITO ORIGINAL:
 * [Descripción de para qué fue creada originalmente esta función]
 * 
 * ESTADO ACTUAL:
 * [Explicar por qué está vacía o no-op actualmente]
 * 
 * RAZÓN DE MANTENERLA:
 * - Compatibilidad con código existente que la invoca
 * - Diseño futuro planificado
 * - Interfaz/contrato que debe cumplirse
 * - Evitar breaking changes en la API pública
 * - Placeholder para implementación pendiente
 * 
 * CONDICIONES PARA ELIMINACIÓN:
 * - [Condición 1: ejemplo: "Cuando se migre completamente a la nueva API"]
 * - [Condición 2: ejemplo: "Cuando no haya invocaciones en el código"]
 * - [Condición 3: ejemplo: "Después de la versión X.Y.Z"]
 * 
 * IMPLEMENTACIÓN FUTURA (opcional):
 * [Descripción de cómo debería implementarse si se decide hacerlo]
 * 
 * @see [Referencias a clases/funciones relacionadas]
 * @since [Versión en que se creó]
 * @deprecated [Si aplica, indicar desde cuándo y alternativa]
 */
@Suppress("UNUSED_PARAMETER") // Si tiene parámetros no usados
fun ejemploNoOp(parametro: String) {
    // No-op: Implementación vacía intencionalmente
}
```

## 🎯 Variaciones de la Plantilla

### Para Funciones Composable No-Op

```kotlin
/**
 * ⚠️ COMPOSABLE NO-OP
 * 
 * PROPÓSITO ORIGINAL: [...]
 * ESTADO ACTUAL: [...]
 * RAZÓN DE MANTENERLA: [...]
 * CONDICIONES PARA ELIMINACIÓN: [...]
 */
@Suppress("UNUSED_PARAMETER")
@Composable
fun EjemploComposableNoOp(param: Int) {
    // No-op: Renderizado vacío intencionalmente
}
```

### Para Funciones con Comentario de Implementación Futura

```kotlin
/**
 * 🚧 FUNCIÓN PLACEHOLDER - IMPLEMENTACIÓN PENDIENTE
 * 
 * PROPÓSITO: [...]
 * ESTADO ACTUAL: Placeholder que no ejecuta lógica
 * RAZÓN DE MANTENERLA: Diseño de API definido, implementación pendiente
 * CONDICIONES PARA IMPLEMENTACIÓN: [...]
 */
fun ejemploPlaceholder() {
    // TODO: Implementar lógica de [descripción]
}
```

### Para Callbacks Vacíos

```kotlin
/**
 * ⚠️ CALLBACK NO-OP
 * 
 * PROPÓSITO ORIGINAL: [...]
 * ESTADO ACTUAL: No ejecuta acción
 * RAZÓN: Se mantiene para evitar null checks en el código llamador
 * CONDICIONES PARA ELIMINACIÓN: Cuando se implemente el manejo real del evento
 */
val onClickNoOp: () -> Unit = { /* No-op callback */ }
```

## 📝 Ejemplos de Uso

### Ejemplo 1: Función de Sincronización Deshabilitada
```text
/**
 * ⚠️ FUNCIÓN NO-OP
 * 
 * PROPÓSITO ORIGINAL:
 * Sincronizar el color de la barra de estado del sistema con el progreso
 * del header colapsable para crear una transición visual fluida.
 * 
 * ESTADO ACTUAL:
 * Deshabilitada a petición del usuario. No modifica la barra de estado.
 * 
 * RAZÓN DE MANTENERLA:
 * - Compatibilidad con código existente que la invoca desde StoreHost
 * - Evitar breaking changes (la función se llama en múltiples lugares)
 * 
 * CONDICIONES PARA ELIMINACIÓN:
 * - Cuando se refactorice StoreHost y se eliminen todas las invocaciones
 * - Después de verificar que no se necesita en ninguna otra pantalla
 * - En la próxima versión mayor (breaking changes permitidos)
 * 
 * @param headerProgress Progreso de colapso del header (0f = expandido, 1f = colapsado)
 * @param initialColor Color inicial de la barra de estado
 * @see StoreHost donde se invoca esta función
 * @since v1.0
 */
@Suppress("UNUSED_PARAMETER")
@Composable
fun StatusBarSync(headerProgress: Float, initialColor: Color = Color(0xFFFFA500)) {
    // No-op: la lógica de modificación de barra de estado ha sido eliminada
}
```

### Ejemplo 2: Feature No Implementada
```text
/**
 * 🚧 FUNCIÓN PLACEHOLDER - IMPLEMENTACIÓN PENDIENTE
 * 
 * PROPÓSITO ORIGINAL:
 * Permitir la compra directa de un producto sin agregarlo al carrito,
 * navegando directamente al checkout con ese único producto.
 * 
 * ESTADO ACTUAL:
 * Placeholder sin implementación. El botón existe en la UI pero no ejecuta acción.
 * 
 * RAZÓN DE MANTENERLA:
 * - Diseño de UX ya definido (botón visible en ProductDetailScreen)
 * - Feature planificada para siguiente sprint
 * - Evita tener que modificar la UI más adelante
 * 
 * CONDICIONES PARA IMPLEMENTACIÓN:
 * - Cuando se complete la pantalla de Checkout
 * - Cuando se implemente el flujo de pago inmediato
 * - Sprint 3 según roadmap del proyecto
 * 
 * IMPLEMENTACIÓN FUTURA:
 * Debe crear un carrito temporal con el producto seleccionado,
 * navegar a la pantalla de checkout y pasar el contexto de "compra rápida".
 * 
 * @param product Producto a comprar directamente
 * @param quantity Cantidad del producto
 * @param onComplete Callback al completar la compra
 */
fun buyNow(product: Product, quantity: Int, onComplete: () -> Unit) {
    // TODO: Implementar compra directa
    // 1. Crear carrito temporal
    // 2. Navegar a checkout con flag buyNow=true
    // 3. Ejecutar onComplete después de pago exitoso
}
```

## 🔍 Guía de Uso

1. **Identificar el tipo de función**: No-op, Placeholder, Callback vacío
2. **Copiar la plantilla apropiada**
3. **Completar cada sección**:
   - ✅ PROPÓSITO ORIGINAL: Siempre obligatorio
   - ✅ ESTADO ACTUAL: Explicar claramente por qué está vacía
   - ✅ RAZÓN DE MANTENERLA: Listar las razones que apliquen
   - ✅ CONDICIONES PARA ELIMINACIÓN: Ser específico y medible
4. **Agregar anotaciones relevantes**: `@Suppress`, `@Deprecated`, `@Composable`
5. **Incluir comentario inline**: Breve recordatorio de que es no-op

## ⚡ Checklist de Calidad

Antes de considerar la documentación completa, verificar:

- ¿Se explica claramente por qué existe la función?
- ¿Se documenta por qué está vacía actualmente?
- ¿Las condiciones de eliminación son específicas y medibles?
- ¿Se agregó `@Suppress` para silenciar warnings relevantes?
- ¿Hay un comentario inline breve para lectura rápida?
- ¿Se referenciaron las ubicaciones donde se usa?
