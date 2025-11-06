@echo off
echo ============================================================
echo ELIMINANDO RECURSOS DUPLICADOS
echo ============================================================
echo.

cd /d "%~dp0\.."

echo Eliminando archivos duplicados de ic_ermotos_background...

if exist "app\src\main\res\values\ic_ermotos_background.xml" (
    del "app\src\main\res\values\ic_ermotos_background.xml"
    echo [OK] Eliminado: values\ic_ermotos_background.xml
) else (
    echo [SKIP] No existe: values\ic_ermotos_background.xml
)

if exist "app\src\main\res\drawable\ic_ermotos_background.xml" (
    del "app\src\main\res\drawable\ic_ermotos_background.xml"
    echo [OK] Eliminado: drawable\ic_ermotos_background.xml
) else (
    echo [SKIP] No existe: drawable\ic_ermotos_background.xml
)

echo.
echo ============================================================
echo COMPLETADO
echo ============================================================
echo.
echo El color ic_ermotos_background ahora solo esta definido en colors.xml
echo Puedes reconstruir el proyecto ahora.
echo.
pause

