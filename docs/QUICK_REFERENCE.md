# 🚀 Guía Rápida de Componentes Reutilizables

## Componentes Creados

### BannerComponents.kt
- CategoryBanner - Banner destacado para categorías
- OfferBanner - Banner promocional con gradientes
- InfoBanner - Mensajes informativos

### CartComponents.kt
- CartItemCard - Tarjeta de item del carrito
- PriceSummaryCard - Resumen de precios con desglose
- EmptyCartCard - Estado vacío del carrito

### BadgeComponents.kt
- StockBadge - Muestra disponibilidad con alertas
- PriceTag - Precios con formato consistente
- StatusBadge - Estados genéricos
- DiscountBadge - Descuentos compactos
- OutOfStockCard - Card para productos agotados

### Componentes Existentes Mejorados
- ProductCard - Tarjeta de producto
- CategoryCard - Tarjeta de categoría

## 📊 Beneficios

- **Reducción de código**: ~500 líneas eliminadas
- **Reutilización**: Componentes usables en múltiples pantallas
- **Mantenibilidad**: Cambios centralizados
- **Type-safe**: Integración directa con modelos del dominio
- **Previews**: Todos incluyen @Preview para desarrollo

## 🔄 Pantallas Refactorizadas

- HomeScreen.kt - Usa CategoryBanner
- CartScreen.kt - Usa CartItemCard, PriceSummaryCard, EmptyCartCard
- ProductDetailScreen.kt - Usa StockBadge, PriceTag, OutOfStockCard

## 📚 Documentación Completa

Ver: docs/COMPONENTES_REUTILIZABLES.md
