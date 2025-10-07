@echo off
REM Script para eliminar archivos legacy (XML/Fragments) y compilar para verificar
REM -- Ejecutar desde la raíz del repositorio en Windows cmd.exe

necho Creando rama de backup 'cleanup/remove-legacy-xml'...
ngit checkout -b cleanup/remove-legacy-xml

necho Haciendo commit de checkpoint (si hay cambios)...
git add -A
ngit commit -m "WIP: checkpoint before removing legacy xml/fragments" || echo "No hay cambios para commitear"

necho Eliminando archivos legacy listados...
ngit rm -f app/src/main/res/layout/activity_login.xml app/src/main/res/layout/activity_register.xml app/src/main/res/layout/fragment_dashboard.xml app/src/main/res/layout/activity_main.xml app/src/main/res/navigation/nav_graph.xml
ngit rm -f app/src/main/java/com/example/auth/LoginFragment.kt app/src/main/java/com/example/auth/RegisterFragment.kt app/src/main/java/com/example/auth/DashboardFragment.kt
ngit rm -f app/src/main/res/drawable/gradient_background.xml app/src/main/res/drawable/bg_card_gradient.xml app/src/main/res/drawable/ic_avatar_bg.xml app/src/main/res/drawable/ic_motorcycle.xml
ngit rm -f app/src/main/res/anim/nav_enter.xml app/src/main/res/anim/nav_exit.xml app/src/main/res/anim/nav_pop_enter.xml app/src/main/res/anim/nav_pop_exit.xml
ngit rm -f app/src/main/res/animator/back_fill_color_animator.xml app/src/main/res/animator/motor_fill_color_animator.xml

necho Commit de los archivos eliminados...
git commit -m "chore: remove legacy fragments/layouts/navigation resources" || echo "No hay cambios luego del git rm"

necho Limpiando y compilando (esto verificara si algo quedó roto)...
gradlew.bat clean assembleDebug

necho Proceso finalizado. Si el build falla, revierte con: git checkout -f HEAD~1 o revisa la rama 'cleanup/remove-legacy-xml'.
echo Nota: revisa el output para ver errores y ejecutar pruebas adicionales.
pause

