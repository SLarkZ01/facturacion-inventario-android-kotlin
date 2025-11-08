# 🔍 Guía: Cómo Verificar si tu Backend está Corriendo en Puerto 8080

## Método 1: Usando el Script Automático ✨ (RECOMENDADO)

1. Ve a la carpeta: `scripts/`
2. Haz doble clic en: `verificar_backend.bat`
3. El script te mostrará:
   - ✅ Si el puerto 8080 está en uso
   - ✅ Si hay procesos Java corriendo
   - ✅ Si el backend responde correctamente

## Método 2: Comando Manual en CMD

Abre el símbolo del sistema (CMD) y ejecuta:

```cmd
netstat -ano | findstr :8080
```

**Resultado esperado:**
- **Si el backend ESTÁ corriendo**: Verás algo como:
  ```
  TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING       12345
  TCP    [::]:8080              [::]:0                 LISTENING       12345
  ```
  
- **Si el backend NO está corriendo**: No verás ninguna salida

## Método 3: Usando el Navegador Web 🌐 (MÁS FÁCIL)

1. Abre tu navegador (Chrome, Firefox, Edge, etc.)
2. Ve a la siguiente URL:
   ```
   http://localhost:8080/api/categorias
   ```

**Resultado esperado:**
- **✅ Backend corriendo**: Verás un JSON con categorías
  ```json
  {
    "categorias": [...],
    "totalElements": 10,
    "totalPages": 1
  }
  ```

- **❌ Backend NO corriendo**: Verás un error:
  - "No se puede acceder a este sitio"
  - "ERR_CONNECTION_REFUSED"
  - "Este sitio no está disponible"

## Método 4: Usando PowerShell

Abre PowerShell y ejecuta:

```powershell
Test-NetConnection -ComputerName localhost -Port 8080
```

**Resultado esperado:**
- **Si está corriendo**: `TcpTestSucceeded : True`
- **Si NO está corriendo**: `TcpTestSucceeded : False`

## Método 5: Revisar el Task Manager

1. Presiona `Ctrl + Shift + Esc` para abrir el Administrador de Tareas
2. Ve a la pestaña "Detalles"
3. Busca procesos llamados `java.exe` o `javaw.exe`
4. Si ves procesos Java con alto uso de memoria (100-500 MB), probablemente sea tu backend

## 🚀 Cómo INICIAR tu Backend de Spring Boot

Si tu backend NO está corriendo, aquí está cómo iniciarlo:

### Opción A: Desde IntelliJ IDEA / Eclipse
1. Abre tu proyecto backend (el que tiene el código Java)
2. Busca la clase principal (normalmente algo como `Application.java` o `BackendApplication.java`)
3. Haz clic derecho → `Run` o presiona el botón ▶️ verde
4. Espera a ver en la consola:
   ```
   Started Application in X seconds (JVM running for Y)
   ```

### Opción B: Desde la línea de comandos
Si usas Maven:
```cmd
cd ruta\a\tu\proyecto\backend
mvnw spring-boot:run
```

Si usas Gradle:
```cmd
cd ruta\a\tu\proyecto\backend
gradlew bootRun
```

### Opción C: Desde un JAR compilado
```cmd
cd ruta\a\tu\proyecto\backend\target
java -jar nombre-del-archivo.jar
```

## 📝 Logs del Backend

Una vez que el backend esté corriendo, deberías ver logs como:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (vX.X.X)

[...] Starting Application...
[...] Tomcat started on port(s): 8080 (http)
[...] Started Application in X seconds
```

## 🔧 Solución de Problemas

### El puerto 8080 ya está en uso por otra aplicación
Si otro programa está usando el puerto 8080:

1. **Encontrar qué proceso lo está usando:**
   ```cmd
   netstat -ano | findstr :8080
   ```
   Verás un número al final (PID), por ejemplo: `12345`

2. **Ver qué aplicación es:**
   ```cmd
   tasklist | findstr 12345
   ```

3. **Opciones:**
   - Cerrar esa aplicación
   - O cambiar el puerto de tu backend Spring Boot en `application.properties`:
     ```properties
     server.port=8081
     ```
     (Y luego actualizar la URL en tu app Android)

### Backend se inicia pero da errores
- Revisa que MongoDB esté corriendo (si tu backend usa MongoDB)
- Verifica las credenciales de la base de datos
- Revisa los logs en la consola del backend

## ✅ Verificación Final

Para estar 100% seguro de que todo funciona:

1. **Backend corriendo** ✓
   - El puerto 8080 está en uso
   - Proceso Java visible en Task Manager

2. **API responde** ✓
   - `http://localhost:8080/api/categorias` devuelve JSON

3. **App Android configurada** ✓
   - Verifica `ApiConfig.BASE_URL` apunta a `http://tu-ip:8080/`
   - Si usas emulador: `http://10.0.2.2:8080/`
   - Si usas dispositivo físico: `http://192.168.X.X:8080/`

## 📱 Configuración para Android

### Si usas Emulador de Android:
```kotlin
// ApiConfig.kt
BASE_URL = "http://10.0.2.2:8080/"
```

### Si usas Dispositivo Físico:
1. Encuentra tu IP local:
   ```cmd
   ipconfig
   ```
   Busca la "Dirección IPv4" (ejemplo: 192.168.1.100)

2. Usa esa IP en tu app:
   ```kotlin
   // ApiConfig.kt
   BASE_URL = "http://192.168.1.100:8080/"
   ```

3. **IMPORTANTE**: Tu teléfono y PC deben estar en la misma red WiFi

## 🎯 Resumen Rápido

**Para verificar:**
1. Ejecuta `verificar_backend.bat` en la carpeta `scripts/`
2. O abre `http://localhost:8080/api/categorias` en tu navegador

**Para iniciar:**
1. Abre tu proyecto backend en IntelliJ/Eclipse
2. Ejecuta la clase principal
3. Espera el mensaje "Started Application"

**Para probar con Android:**
1. Backend corriendo ✓
2. URL correcta en `ApiConfig.BASE_URL` ✓
3. Ejecuta tu app Android ✓

---

¿Necesitas ayuda con algún paso específico? 🚀

