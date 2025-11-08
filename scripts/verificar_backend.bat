@echo off
echo ========================================
echo   Verificando Backend en Puerto 8080
echo ========================================
echo.

echo [1] Verificando si el puerto 8080 esta en uso...
netstat -ano | findstr :8080
if %errorlevel% equ 0 (
    echo.
    echo ✅ El puerto 8080 ESTA EN USO
    echo.
    echo Detalles de las conexiones:
    netstat -ano | findstr :8080
) else (
    echo.
    echo ❌ El puerto 8080 NO esta en uso
    echo    Tu backend NO esta corriendo
)

echo.
echo ========================================
echo [2] Buscando procesos Java (Spring Boot)...
tasklist | findstr /I "java.exe"
if %errorlevel% equ 0 (
    echo.
    echo ✅ Se encontraron procesos Java
) else (
    echo.
    echo ❌ No se encontraron procesos Java
)

echo.
echo ========================================
echo [3] Intentando conectar al backend...
echo.
curl -s -o nul -w "Status: %%{http_code}\n" http://localhost:8080/api/categorias 2>nul
if %errorlevel% equ 0 (
    echo ✅ Backend responde correctamente
) else (
    echo ❌ No se pudo conectar al backend
    echo    Verifica que curl este instalado o usa un navegador
)

echo.
echo ========================================
echo [4] INSTRUCCIONES:
echo ========================================
echo.
echo Si el puerto 8080 NO esta en uso:
echo   1. Abre tu proyecto backend de Spring Boot
echo   2. Ejecuta el main de tu aplicacion
echo   3. Espera a ver: "Started Application in X seconds"
echo.
echo Si el puerto 8080 ESTA en uso:
echo   1. Abre un navegador
echo   2. Ve a: http://localhost:8080/api/categorias
echo   3. Deberias ver un JSON con categorias
echo.
echo Para ver los logs del backend:
echo   - Revisa la consola donde ejecutaste Spring Boot
echo.
echo ========================================
pause

