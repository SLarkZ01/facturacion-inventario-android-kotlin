@echo off
echo ====================================
echo Limpiando proyecto y cache de Kotlin
echo ====================================

cd /d "%~dp0"

echo.
echo [1/4] Limpiando build...
call gradlew.bat clean

echo.
echo [2/4] Eliminando caches de Kotlin...
rmdir /s /q .gradle\kotlin 2>nul
rmdir /s /q app\build\kotlin 2>nul
rmdir /s /q build\kotlin 2>nul
rmdir /s /q .kotlin 2>nul

echo.
echo [3/4] Eliminando caches de compilacion...
rmdir /s /q app\build\tmp 2>nul
rmdir /s /q app\build\intermediates 2>nul

echo.
echo [4/4] Compilando proyecto...
call gradlew.bat assembleDebug

echo.
echo ====================================
echo Proceso completado
echo ====================================
pause

