# Configuración del Ícono Personalizado de Ermotos

## ✅ Cambios Realizados

### 1. **AndroidManifest.xml actualizado**
   - Cambiado `android:icon="@mipmap/ic_launcher"` → `@mipmap/ic_ermotos`
   - Cambiado `android:roundIcon="@mipmap/ic_launcher_round"` → `@mipmap/ic_ermotos_round`

### 2. **Color de fondo agregado**
   - Agregado `ic_ermotos_background` en `res/values/colors.xml`
   - Color naranja vibrante (#FF6200) que combina con tu marca

### 3. **Scripts creados**
   - `setup_ermotos_icon.py`: Script Python para generar íconos en todas las resoluciones
   - `setup_ermotos_icon.bat`: Script batch para ejecutar fácilmente

## 📋 Pasos para Completar la Configuración

### Opción 1: Usar los íconos existentes (Recomendado - Más Rápido)
Si los íconos `ic_ermotos` ya existen en las carpetas mipmap, solo necesitas:

1. **Limpiar el proyecto:**
   ```
   ./gradlew clean
   ```

2. **Reconstruir:**
   ```
   ./gradlew build
   ```

3. **Ejecutar la app:**
   - El ícono de Ermotos ya debería aparecer en lugar del ícono verde de Android

### Opción 2: Regenerar íconos desde ermotoshd.png

Si quieres regenerar los íconos desde cero usando tu logo:

1. **Instalar Pillow (si no lo tienes):**
   ```
   pip install Pillow
   ```

2. **Ejecutar el script:**
   - Doble clic en `scripts/setup_ermotos_icon.bat`
   - O desde terminal: `python scripts/setup_ermotos_icon.py`

3. **Verificar los íconos generados:**
   - Se crearán archivos `.webp` en todas las carpetas mipmap
   - Densidades: mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi

4. **Limpiar y reconstruir:**
   ```
   ./gradlew clean build
   ```

## 📂 Estructura de Íconos Generados

```
res/
├── mipmap-anydpi-v26/
│   ├── ic_ermotos.xml (adaptive icon)
│   └── ic_ermotos_round.xml (adaptive icon redondo)
│
├── mipmap-mdpi/
│   ├── ic_ermotos.webp (48x48)
│   ├── ic_ermotos_round.webp (48x48)
│   └── ic_ermotos_foreground.webp (108x108)
│
├── mipmap-hdpi/
│   ├── ic_ermotos.webp (72x72)
│   ├── ic_ermotos_round.webp (72x72)
│   └── ic_ermotos_foreground.webp (162x162)
│
├── mipmap-xhdpi/
│   ├── ic_ermotos.webp (96x96)
│   ├── ic_ermotos_round.webp (96x96)
│   └── ic_ermotos_foreground.webp (216x216)
│
├── mipmap-xxhdpi/
│   ├── ic_ermotos.webp (144x144)
│   ├── ic_ermotos_round.webp (144x144)
│   └── ic_ermotos_foreground.webp (324x324)
│
└── mipmap-xxxhdpi/
    ├── ic_ermotos.webp (192x192)
    ├── ic_ermotos_round.webp (192x192)
    └── ic_ermotos_foreground.webp (432x432)
```

## 🎨 Personalización del Color de Fondo

Si quieres cambiar el color de fondo del ícono adaptativo, edita en `res/values/colors.xml`:

```xml
<color name="ic_ermotos_background">#FF6200</color>
```

Colores sugeridos para Ermotos:
- **Naranja vibrante:** `#FF6200` (actual)
- **Naranja primario:** `#FF6B35`
- **Blanco:** `#FFFFFF`
- **Negro:** `#000000`

## ✨ Tipos de Íconos Incluidos

### 1. **Adaptive Icons (Android 8.0+)**
   - Se adaptan a diferentes formas según el fabricante
   - Incluyen foreground (logo) y background (color sólido)
   - Soportan animaciones y efectos visuales

### 2. **Íconos Legacy**
   - Para versiones anteriores a Android 8.0
   - Formato cuadrado estándar
   - Versión redonda incluida

## 🔍 Verificación

Para verificar que todo funciona:

1. **En Android Studio:**
   - Ve a `res/mipmap-anydpi-v26/ic_ermotos.xml`
   - Deberías ver la vista previa del ícono adaptativo

2. **En el dispositivo/emulador:**
   - Instala la app
   - El ícono de Ermotos debería aparecer en el launcher
   - En dispositivos con Android 8.0+, verás el ícono adaptativo

3. **Verifica todas las densidades:**
   - Abre cada carpeta mipmap y verifica que existan los archivos

## 🚨 Solución de Problemas

### El ícono no cambia después de instalar
- **Solución:** Desinstala completamente la app y vuelve a instalar
- O limpia la caché del launcher: Configuración → Apps → Launcher → Limpiar caché

### Error "Resource not found"
- **Causa:** Faltan archivos de íconos
- **Solución:** Ejecuta el script `setup_ermotos_icon.bat` para generar todos los archivos

### El ícono se ve pixelado
- **Causa:** Falta alguna densidad
- **Solución:** Verifica que existan archivos en todas las carpetas mipmap

### Pillow no está instalado
```
pip install Pillow
```

## 📱 Resultado Final

Una vez completados estos pasos, tu app mostrará:
- ✅ Logo de Ermotos en el launcher (reemplazando el ícono verde de Android)
- ✅ Ícono adaptativo en dispositivos modernos
- ✅ Versiones optimizadas para cada densidad de pantalla
- ✅ Compatibilidad con todas las versiones de Android (API 24+)

## 🎯 Próximos Pasos Opcionales

Si quieres mejorar aún más el ícono:

1. **Splash Screen personalizado:** Configura un splash screen con el logo de Ermotos
2. **Ícono foreground mejorado:** Crea un diseño específico para el foreground
3. **Monochrome icon:** Agrega un ícono monocromático para Android 13+ themed icons

---

**Nota:** Los cambios en el AndroidManifest.xml ya están aplicados. Solo necesitas limpiar y reconstruir el proyecto para ver el nuevo ícono.

