# 🔧 Pasos para Arreglar el Backend (Error 401 al Agregar al Carrito)

## ⚡ Resumen del Problema
El backend permite crear y consultar carritos, pero bloquea la operación de **agregar items** al carrito con error 401.

---

## 📋 PASOS A SEGUIR (5 minutos)

### 1️⃣ Abre el archivo SecurityConfig.java

**Ubicación:**
```
inventario-repuestos-backend/src/main/java/com/repobackend/api/auth/config/SecurityConfig.java
```

### 2️⃣ Busca esta línea:

```java
.requestMatchers("/api/carritos/**").permitAll()
```

### 3️⃣ Verifica que tenga `/**` al final

✅ **CORRECTO:**
```java
.requestMatchers("/api/carritos/**").permitAll()  // con /**
```

❌ **INCORRECTO:**
```java
.requestMatchers("/api/carritos").permitAll()     // sin /**
.requestMatchers("/api/carritos/*").permitAll()   // con solo /*
```

El `/**` es crucial porque permite TODAS las sub-rutas:
- `/api/carritos` ✅
- `/api/carritos/{id}` ✅
- `/api/carritos/{id}/items` ✅ ← **ESTO ES LO QUE ESTABA BLOQUEADO**

### 4️⃣ Si no lo tienes, agrega esta configuración completa:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/api/productos/**").permitAll()
            .requestMatchers("/api/categorias/**").permitAll()
            .requestMatchers("/api/carritos/**").permitAll()      // ← ESTA LÍNEA
            .requestMatchers("/api/favoritos/**").permitAll()
            .anyRequest().authenticated()
        );
    
    return http.build();
}
```

### 5️⃣ Guarda el archivo (Ctrl + S)

### 6️⃣ Reinicia el backend COMPLETAMENTE

**En la terminal:**
```bash
# Detén el servidor (Ctrl + C)

# Luego ejecuta con clean para asegurar que se apliquen los cambios:
cd C:\Users\Danie\OneDrive\Documentos\PROYECTOS-FACTURACION-INVENTARIO\inventario-repuestos-backend
./mvnw clean spring-boot:run
```

**Espera a ver:**
```
Started InventarioRepuestosBackendApplication in X.XXX seconds
```

---

## ✅ Verificación Rápida (PowerShell)

Antes de probar en la app, verifica que funciona:

```powershell
# 1. Crear un carrito
$headers = @{ "Content-Type" = "application/json" }
$body = '{"items":[]}'
$carrito = Invoke-RestMethod -Uri "http://localhost:8080/api/carritos" -Method POST -Headers $headers -Body $body
$carritoId = $carrito.carrito.id

# 2. Agregar un item (ESTO es lo que fallaba)
$itemBody = '{"productoId":"690f7c95c989e80f1c0afc78","cantidad":1,"precioUnitario":35.0}'
Invoke-RestMethod -Uri "http://localhost:8080/api/carritos/$carritoId/items" -Method POST -Headers $headers -Body $itemBody
```

**Resultado esperado:**
- ✅ Sin error 401
- ✅ Respuesta con el carrito que incluye el item

**Si ves error 401:**
- El cambio no se aplicó
- Verifica que la línea tenga `/**`
- Reinicia con `clean`

---

## 🎯 Resultado Final

Después de estos pasos:
- ✅ Podrás agregar productos al carrito desde la app
- ✅ Los productos aparecerán en el carrito
- ✅ No más error 401

---

## 📝 Archivo de Referencia

Tengo un archivo completo en:
```
backend-config/SecurityConfig.java
```

Puedes copiar ese archivo completo si prefieres reemplazar todo el contenido.

---

**Tiempo estimado:** 5 minutos  
**Dificultad:** ⭐ Fácil  
**Requiere reiniciar backend:** ✅ Sí

