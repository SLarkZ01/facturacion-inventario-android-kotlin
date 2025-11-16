@echo off
echo ========================================
echo VERIFICACION DE API DE CATEGORIAS
echo ========================================
echo.

echo [1] Probando endpoint de categorias GLOBALES (por defecto)
echo GET http://localhost:8080/api/categorias
curl -X GET "http://localhost:8080/api/categorias" -H "Content-Type: application/json"
echo.
echo.

echo [2] Probando endpoint con parametro todas=true
echo GET http://localhost:8080/api/categorias?todas=true
curl -X GET "http://localhost:8080/api/categorias?todas=true" -H "Content-Type: application/json"
echo.
echo.

echo [3] Probando con size mayor para ver todas
echo GET http://localhost:8080/api/categorias?todas=true^&size=100
curl -X GET "http://localhost:8080/api/categorias?todas=true&size=100" -H "Content-Type: application/json"
echo.
echo.

echo ========================================
echo PRUEBA COMPLETADA
echo ========================================
echo.
echo Si ves categorias con "tallerId": null son GLOBALES
echo Si ves categorias con "tallerId": "xxx..." son DE TALLER
echo.
pause

