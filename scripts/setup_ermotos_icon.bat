@echo off
echo ============================================================
echo CONFIGURACION DE ICONO ERMOTOS
echo ============================================================
echo.

cd /d "%~dp0"
python setup_ermotos_icon.py

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================================
    echo ICONOS GENERADOS CORRECTAMENTE
    echo ============================================================
    echo.
    echo Ahora actualizando AndroidManifest.xml...
) else (
    echo.
    echo ERROR: No se pudieron generar los iconos
    echo Verifica que Pillow este instalado: pip install Pillow
    pause
    exit /b 1
)

pause

