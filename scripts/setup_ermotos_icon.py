"""
Script para configurar el ícono de Ermotos en la app Android
Genera todos los tamaños necesarios desde ermotoshd.png
"""

from PIL import Image
import os
import sys

# Rutas del proyecto
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
PROJECT_ROOT = os.path.dirname(SCRIPT_DIR)
APP_DIR = os.path.join(PROJECT_ROOT, "app", "src", "main")
RES_DIR = os.path.join(APP_DIR, "res")
SOURCE_IMAGE = os.path.join(RES_DIR, "drawable", "ermotoshd.png")

# Tamaños para cada densidad de pantalla
ICON_SIZES = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192
}

# Tamaños para foreground (108dp con padding)
FOREGROUND_SIZES = {
    "mipmap-mdpi": 108,
    "mipmap-hdpi": 162,
    "mipmap-xhdpi": 216,
    "mipmap-xxhdpi": 324,
    "mipmap-xxxhdpi": 432
}

def create_icon(source_path, output_path, size):
    """Crea un ícono cuadrado con el logo centrado"""
    try:
        # Abrir imagen original
        img = Image.open(source_path)

        # Convertir a RGBA si no lo es
        if img.mode != 'RGBA':
            img = img.convert('RGBA')

        # Redimensionar manteniendo aspecto
        img.thumbnail((size, size), Image.Resampling.LANCZOS)

        # Crear imagen cuadrada con transparencia
        new_img = Image.new('RGBA', (size, size), (0, 0, 0, 0))

        # Centrar la imagen
        x = (size - img.width) // 2
        y = (size - img.height) // 2
        new_img.paste(img, (x, y), img)

        # Guardar como WebP
        new_img.save(output_path, 'WEBP', quality=95)
        print(f"✓ Creado: {output_path}")
        return True
    except Exception as e:
        print(f"✗ Error creando {output_path}: {e}")
        return False

def create_foreground_icon(source_path, output_path, size):
    """Crea un ícono foreground con padding para adaptive icons"""
    try:
        # Abrir imagen original
        img = Image.open(source_path)

        # Convertir a RGBA
        if img.mode != 'RGBA':
            img = img.convert('RGBA')

        # El área segura es 66% del total (72dp de 108dp)
        safe_size = int(size * 0.66)

        # Redimensionar al área segura
        img.thumbnail((safe_size, safe_size), Image.Resampling.LANCZOS)

        # Crear imagen con padding
        new_img = Image.new('RGBA', (size, size), (0, 0, 0, 0))

        # Centrar
        x = (size - img.width) // 2
        y = (size - img.height) // 2
        new_img.paste(img, (x, y), img)

        # Guardar
        new_img.save(output_path, 'WEBP', quality=95)
        print(f"✓ Creado: {output_path}")
        return True
    except Exception as e:
        print(f"✗ Error creando {output_path}: {e}")
        return False

def create_adaptive_icon_xml(output_dir, icon_name):
    """Crea el XML para adaptive icon"""
    xml_content = f"""<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@color/{icon_name}_background"/>
    <foreground android:drawable="@mipmap/{icon_name}_foreground"/>
</adaptive-icon>"""

    # Crear archivos XML para ambas versiones
    for variant in ["", "_round"]:
        xml_path = os.path.join(output_dir, f"{icon_name}{variant}.xml")
        with open(xml_path, 'w', encoding='utf-8') as f:
            f.write(xml_content)
        print(f"✓ Creado: {xml_path}")

def main():
    print("=" * 60)
    print("CONFIGURACIÓN DE ÍCONO ERMOTOS")
    print("=" * 60)

    # Verificar que existe la imagen fuente
    if not os.path.exists(SOURCE_IMAGE):
        print(f"✗ Error: No se encuentra {SOURCE_IMAGE}")
        return False

    print(f"\n✓ Imagen fuente encontrada: {SOURCE_IMAGE}")

    # Verificar que PIL/Pillow está instalado
    try:
        img = Image.open(SOURCE_IMAGE)
        print(f"✓ Dimensiones de la imagen: {img.width}x{img.height}")
    except Exception as e:
        print(f"✗ Error al abrir la imagen: {e}")
        return False

    success_count = 0
    total_count = 0

    # Generar íconos principales y redondos
    print("\n--- Generando íconos principales ---")
    for density, size in ICON_SIZES.items():
        output_dir = os.path.join(RES_DIR, density)
        os.makedirs(output_dir, exist_ok=True)

        # Ícono normal
        icon_path = os.path.join(output_dir, "ic_ermotos.webp")
        if create_icon(SOURCE_IMAGE, icon_path, size):
            success_count += 1
        total_count += 1

        # Ícono redondo (mismo contenido)
        round_path = os.path.join(output_dir, "ic_ermotos_round.webp")
        if create_icon(SOURCE_IMAGE, round_path, size):
            success_count += 1
        total_count += 1

    # Generar íconos foreground para adaptive icons
    print("\n--- Generando íconos foreground (adaptive) ---")
    for density, size in FOREGROUND_SIZES.items():
        output_dir = os.path.join(RES_DIR, density)
        os.makedirs(output_dir, exist_ok=True)

        foreground_path = os.path.join(output_dir, "ic_ermotos_foreground.webp")
        if create_foreground_icon(SOURCE_IMAGE, foreground_path, size):
            success_count += 1
        total_count += 1

    # Crear XMLs para adaptive icons
    print("\n--- Creando archivos XML para adaptive icons ---")
    anydpi_dir = os.path.join(RES_DIR, "mipmap-anydpi-v26")
    os.makedirs(anydpi_dir, exist_ok=True)
    create_adaptive_icon_xml(anydpi_dir, "ic_ermotos")

    # Verificar/crear color de fondo
    print("\n--- Verificando color de fondo ---")
    colors_file = os.path.join(RES_DIR, "values", "colors.xml")
    if os.path.exists(colors_file):
        with open(colors_file, 'r', encoding='utf-8') as f:
            content = f.read()
            if 'ic_ermotos_background' in content:
                print("✓ Color de fondo ya existe en colors.xml")
            else:
                print("⚠ Nota: Debes agregar el color 'ic_ermotos_background' a colors.xml")
                print("  Ejemplo: <color name=\"ic_ermotos_background\">#FF6200</color>")

    # Resumen
    print("\n" + "=" * 60)
    print(f"RESUMEN: {success_count}/{total_count} íconos creados exitosamente")
    print("=" * 60)

    if success_count == total_count:
        print("\n✓ ¡Íconos generados correctamente!")
        print("\nPróximos pasos:")
        print("1. Verifica que colors.xml tenga el color 'ic_ermotos_background'")
        print("2. Actualiza AndroidManifest.xml para usar @mipmap/ic_ermotos")
        print("3. Limpia y reconstruye el proyecto")
        return True
    else:
        print("\n⚠ Algunos íconos no se pudieron crear")
        return False

if __name__ == "__main__":
    try:
        success = main()
        sys.exit(0 if success else 1)
    except Exception as e:
        print(f"\n✗ Error inesperado: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)

