# FacturacionInventario 🚀📦

Este repositorio contiene la aplicación Android "FacturacionInventario" desarrollada con Kotlin y Android Gradle Plugin.

## Resumen rápido ✅
- Lenguaje: Kotlin
- SDK compile/target: 36
- minSdk: 24
- JDK: 11
- Gradle Wrapper: 8.13
- Kotlin: 2.0.21
- Android Gradle Plugin (AGP): 8.13.0

> Estas versiones se obtuvieron de los archivos del proyecto (app/build.gradle.kts, gradle/libs.versions.toml y gradle/wrapper/gradle-wrapper.properties).

---

## Requisitos previos 🛠️
- Android Studio (recomendado) o SDK/CLI configurado
- JDK 11 instalado
- Android SDK con API 36 (o superior) instalado
- Git

Si usas la línea de comandos en Windows, usa `cmd.exe` o PowerShell.

Si Android Studio no detecta el SDK, crea o actualiza `local.properties` con la ruta del SDK:

Windows example:

```
sdk.dir=C:\Users\TuUsuario\AppData\Local\Android\Sdk
```

---

## Clonar el proyecto 📥

1. Clona el repositorio (reemplaza la URL por la del remoto):

```bash
git clone https://github.com/tu-org/FacturacionInventario.git
cd FacturacionInventario
```

2. Alternativamente, abre Android Studio y selecciona "Get from VCS" → pega la URL del repositorio.

---

## Ejecutar el proyecto (Android Studio) ▶️
1. Abre Android Studio.
2. Selecciona "Open" y abre la carpeta del proyecto.
3. Deja que gradle descargue dependencias y sincronice.
4. Conecta un dispositivo físico o levanta un emulador (AVD con API >= 24).
5. Ejecuta la app con el botón Run (Shift+F10) o selecciona `app` → Run 'app'.

---

## Ejecutar desde la línea de comandos (Windows) 🧰
En `cmd.exe` dentro de la carpeta del proyecto:

```bash
:: Compilar APK debug
.\gradlew.bat assembleDebug

:: Instalar en dispositivo conectado (requiere adb)
.\gradlew.bat installDebug

:: Ejecutar tests unitarios
.\gradlew.bat test
```

Nota: puedes usar `./gradlew` en sistemas Unix/macOS.

---

## Estructura y tecnologías principales 🧭
- Kotlin
- Retrofit + OkHttp para networking
- Moshi / Gson para JSON
- Coroutines para concurrencia
- AndroidX (core-ktx, appcompat, material, lifecycle)

Archivos relevantes:
- `app/src/main/java` → código fuente
- `app/src/main/res` → recursos
- `app/build.gradle.kts` → configuración del módulo
- `gradle/libs.versions.toml` → versiones centralizadas

---

## Cómo colaborar 🤝 (flujo recomendado)
Usa un flujo Git simple y claro para el equipo:

1. Crea una rama por tarea/feature:

```bash
git checkout -b feat/nombre-descriptivo
```

2. Trabaja en la rama y haz commits pequeños y atómicos:

- Mensajes en español (o inglés si ya es la convención del equipo).
- Formato sugerido: `tipo(scope): descripción` — p.ej. `feat(login): agregar refresh token`.

3. Push y abre un Pull Request (PR) a `main` o `develop`:

```bash
git push origin feat/nombre-descriptivo
```

4. Revisión de código:
- Asigna 1-2 reviewers
- Verifica que los cambios compilan y pasan tests
- Prefiere PRs pequeñas para revisión más rápida

5. Merge:
- Usa "Squash and merge" o "Merge commit" según política del equipo
- Protege la rama `main` obligando PRs y revisiones

---

## Convenciones y buenas prácticas 🧾
- Kotlin: usa `kotlin.code.style=official` (ya configurado)
- Evitar hardcodear secretos: usa variables de entorno o keystore seguro
- Documenta cambios importantes en el PR
- Añade tests unitarios para lógica crítica

---

## Hooks útiles / CI (sugerencias) ⚙️
- Configurar CI (GitHub Actions / GitLab CI) para:
  - Ejecutar `./gradlew test` en cada PR
  - Ejecutar lint (Android Lint)
  - Construir flavor debug/release

- Añadir un archivo `./gradle.properties` o secrets en el CI para claves privadas si es necesario.

---

## Problemas comunes y soluciones rápidas 🩺
- Error: "SDK location not found" → configura `local.properties` con `sdk.dir` o abre Android Studio y configúralo.
- Error de versión JDK → instala JDK 11 y configura `JAVA_HOME`.
- Dependencias no descargan → ejecutar `.\gradlew.bat --refresh-dependencies`.

---

## Requisito importante: Backend en Spring Boot 🖥️⚙️
La aplicación cliente Android requiere que el backend esté corriendo para funcionar correctamente. El backend del proyecto está disponible en:

https://github.com/SLarkZ01/inventario-repuestos-backend

Pasos para poner el backend en marcha (localmente):

1. Clona el repo del backend:

```bash
git clone https://github.com/SLarkZ01/inventario-repuestos-backend.git
cd inventario-repuestos-backend
```

2. Arrancar el backend (usa el wrapper que incluya el repo):

- Si el proyecto usa Maven wrapper (`mvnw`): 

```bash
# Windows (cmd.exe o PowerShell)
.\mvnw spring-boot:run
```

- Si el proyecto usa Gradle wrapper (`gradlew`):

```bash
# Windows (cmd.exe o PowerShell)
.\gradlew.bat bootRun
```

Por defecto Spring Boot usa el puerto 8080. Si quieres cambiarlo, edita `src/main/resources/application.properties` o `application.yml` del backend y modifica `server.port`, por ejemplo:

```
server.port=9090
```

O inicia la app con una variable de entorno / argumento:

```bash
# Usando variable de entorno
set SERVER_PORT=9090
.\mvnw spring-boot:run

# O con argumento
.\mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

3. Verifica que el backend responde en http://localhost:8080 (o el puerto configurado).

Cómo configurar la app Android para apuntar al backend

- En este proyecto la URL base del backend se pasa directamente al constructor de `AuthRepository`. Actualmente las actividades `LoginActivity` y `RegisterActivity` usan la URL de emulador:

```
repo = AuthRepository(this, "http://10.0.2.2:8080/")
```

Notas importantes:
- Si usas el emulador Android incluido en Android Studio, `10.0.2.2` apunta al `localhost` de tu máquina host. Por eso la URL `http://10.0.2.2:8080/` funciona para el emulador.
- Si pruebas en un dispositivo físico, reemplaza `10.0.2.2` por la IP local de tu máquina (p. ex. `http://192.168.1.42:8080/`) o expón el backend con una herramienta como ngrok y usa la URL pública.

Sugerencia para producción / buen manejo de la URL:
- Sería conveniente mover la URL base a un sólo lugar (p. ex. `BuildConfig`, `local.properties` o `strings.xml`) en lugar de hardcodearla en múltiples actividades. Esto facilita cambiar el host/puerto según el entorno (dev/staging/prod).

---

## Próximos pasos sugeridos 🚧
- Añadir `CONTRIBUTING.md` con reglas de commit y formato de PR
- Añadir `CODE_OF_CONDUCT.md` si el equipo lo requiere
- Configurar CI básico para pruebas y lint

---

Si quieres, puedo generar un `CONTRIBUTING.md` y una plantilla de PR/ISSUE para el repositorio. ¿Lo hago ahora? ✍️
