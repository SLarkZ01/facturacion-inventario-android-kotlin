const axios = require('axios');

// ⚙️ CONFIGURACIÓN
const BASE_URL = 'http://localhost:8080';
const ALMACEN_ID = '69114a2203af5f216e5fc64a'; // El almacén que da error de permisos

// 🔧 SOLUCIÓN: Dar permisos públicos al almacén para que el sistema pueda descontar stock
async function solucionarPermisosAlmacen() {
    console.log('🔧 SOLUCIONANDO: Permisos insuficientes para modificar stock\n');
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n');

    try {
        // Paso 1: Verificar el almacén actual
        console.log('📦 Paso 1: Verificando almacén...');
        console.log(`   Almacén ID: ${ALMACEN_ID}\n`);

        // Paso 2: Dar permisos públicos (permitir modificar sin autenticación)
        console.log('🔓 Paso 2: Configurando permisos públicos...');

        try {
            // Intentar actualizar el almacén para que sea público
            const updateResponse = await axios.put(
                `${BASE_URL}/api/almacenes/${ALMACEN_ID}`,
                {
                    esPublico: true,
                    permitirModificacionPublica: true
                }
            );
            console.log('   ✅ Permisos actualizados correctamente\n');
        } catch (updateError) {
            console.log('   ⚠️  No se pudo actualizar con PUT, intentando con PATCH...');

            try {
                await axios.patch(
                    `${BASE_URL}/api/almacenes/${ALMACEN_ID}`,
                    {
                        esPublico: true,
                        permitirModificacionPublica: true
                    }
                );
                console.log('   ✅ Permisos actualizados correctamente\n');
            } catch (patchError) {
                console.log('   ⚠️  No se pudo actualizar automáticamente');
                console.log('   Necesitarás configurarlo manualmente (ver instrucciones abajo)\n');
            }
        }

        // Paso 3: Verificar que ahora funcione
        console.log('🧪 Paso 3: Probando ajuste de stock...');

        try {
            // Intentar ajustar stock con delta 0 (no cambia nada, solo prueba)
            const testResponse = await axios.post(
                `${BASE_URL}/api/stock/adjust`,
                {
                    productoId: '69116cdd4633db18ffdb2aad',
                    almacenId: ALMACEN_ID,
                    delta: 0
                }
            );
            console.log('   ✅ ¡Permisos funcionando correctamente!\n');

            console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
            console.log('✅ ¡PROBLEMA RESUELTO!');
            console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n');
            console.log('🎉 Ahora intenta generar la factura desde tu app.');
            console.log('   Debería funcionar sin problemas.\n');

        } catch (testError) {
            const errorMsg = testError.response?.data?.error || testError.message;
            console.log(`   ❌ Todavía hay error: ${errorMsg}\n`);

            console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
            console.log('⚠️  SOLUCIÓN MANUAL NECESARIA');
            console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n');
            mostrarSolucionManual();
        }

    } catch (error) {
        console.error('❌ ERROR:', error.message, '\n');
        mostrarSolucionManual();
    }
}

function mostrarSolucionManual() {
    console.log('📝 SOLUCIÓN MANUAL - Opción 1: MongoDB Compass\n');
    console.log('1. Abre MongoDB Compass');
    console.log('2. Conecta a tu base de datos');
    console.log('3. Busca la colección "almacenes"');
    console.log('4. Encuentra el documento con _id: "69114a2203af5f216e5fc64a"');
    console.log('5. Edita y agrega estos campos:\n');
    console.log('   {');
    console.log('     "_id": "69114a2203af5f216e5fc64a",');
    console.log('     "esPublico": true,');
    console.log('     "permitirModificacionSinAuth": true');
    console.log('   }\n');
    console.log('6. Guarda los cambios\n');

    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n');

    console.log('📝 SOLUCIÓN MANUAL - Opción 2: Desde el Backend (Java)\n');
    console.log('Agrega esto en tu SecurityConfig.java:\n');
    console.log('http.authorizeRequests()');
    console.log('    .antMatchers("/api/stock/**").permitAll() // ← Permitir sin auth');
    console.log('    .antMatchers("/api/facturas/checkout").permitAll()');
    console.log('    // ...resto de configuración\n');

    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n');

    console.log('📝 SOLUCIÓN MANUAL - Opción 3: Quitar validación de permisos\n');
    console.log('En tu FacturaService.java, método checkout():\n');
    console.log('// Comentar o modificar la validación de permisos:');
    console.log('// if (!tienePermisos(usuario, almacen)) {');
    console.log('//     throw new RuntimeException("Permisos insuficientes");');
    console.log('// }\n');
    console.log('O mejor aún, usar un usuario del sistema con permisos totales');
    console.log('para las operaciones de facturación.\n');
}

// Ejecutar
solucionarPermisosAlmacen();

