/**
 * Script de ejemplo para actualizar stock usando la API desde Node.js
 *
 * USO:
 * node actualizar_stock.js
 *
 * REQUISITOS:
 * - npm install axios
 * - Backend corriendo en http://localhost:8080
 * - Token JWT válido (si los endpoints requieren autenticación)
 */

const axios = require('axios');

const BASE_URL = 'http://localhost:8080';

// Configuración de autenticación (si es necesario)
const AUTH_TOKEN = 'tu_token_jwt_aqui'; // Reemplazar con token real si se requiere

// Cliente axios configurado
const apiClient = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    // Descomentar si los endpoints requieren autenticación:
    // 'Authorization': `Bearer ${AUTH_TOKEN}`
  }
});

/**
 * Obtiene el stock de un producto
 * GET /api/stock?productoId={id}
 */
async function obtenerStock(productoId) {
  try {
    console.log(`\n🔍 Consultando stock del producto: ${productoId}`);
    const response = await apiClient.get(`/api/stock`, {
      params: { productoId }
    });

    console.log('✅ Stock obtenido:');
    console.log(`   Total: ${response.data.total} unidades`);
    console.log('   Desglose por almacén:');
    response.data.stockByAlmacen.forEach(almacen => {
      console.log(`     - ${almacen.almacenNombre}: ${almacen.cantidad} unidades`);
    });

    return response.data;
  } catch (error) {
    console.error('❌ Error al obtener stock:', error.response?.data || error.message);
    throw error;
  }
}

/**
 * Ajusta el stock de un producto (incrementa o decrementa)
 * POST /api/stock/adjust
 * 🔐 Requiere autenticación
 */
async function ajustarStock(productoId, almacenId, delta) {
  try {
    console.log(`\n🔧 Ajustando stock: producto=${productoId}, almacen=${almacenId}, delta=${delta}`);
    const response = await apiClient.post('/api/stock/adjust', {
      productoId,
      almacenId,
      delta
    });

    console.log('✅ Stock ajustado exitosamente:');
    console.log(`   Nuevo total: ${response.data.total} unidades`);
    console.log(`   Almacén: ${response.data.stock.cantidad} unidades`);

    return response.data;
  } catch (error) {
    console.error('❌ Error al ajustar stock:', error.response?.data || error.message);
    throw error;
  }
}

/**
 * Establece el stock absoluto de un producto en un almacén
 * POST /api/stock/set
 * 🔐 Requiere autenticación
 */
async function establecerStock(productoId, almacenId, cantidad) {
  try {
    console.log(`\n🔧 Estableciendo stock: producto=${productoId}, almacen=${almacenId}, cantidad=${cantidad}`);
    const response = await apiClient.post('/api/stock/set', {
      productoId,
      almacenId,
      cantidad
    });

    console.log('✅ Stock establecido exitosamente:');
    console.log(`   Nuevo total: ${response.data.total} unidades`);
    console.log(`   Almacén: ${response.data.stock.cantidad} unidades`);

    return response.data;
  } catch (error) {
    console.error('❌ Error al establecer stock:', error.response?.data || error.message);
    throw error;
  }
}

/**
 * Ejemplo de uso: Script principal
 */
async function main() {
  try {
    // IDs de ejemplo - REEMPLAZAR CON IDs REALES DE TU BASE DE DATOS
    const PRODUCTO_ID = '507f191e810c19729de860ea';
    const ALMACEN_ID = '507f1f77bcf86cd799439011';

    console.log('='.repeat(60));
    console.log('📦 DEMO: Gestión de Stock desde Node.js');
    console.log('='.repeat(60));

    // 1. Consultar stock actual
    const stockActual = await obtenerStock(PRODUCTO_ID);

    // 2. Ajustar stock (decrementar 5 unidades)
    // await ajustarStock(PRODUCTO_ID, ALMACEN_ID, -5);

    // 3. Establecer stock absoluto (100 unidades)
    // await establecerStock(PRODUCTO_ID, ALMACEN_ID, 100);

    // 4. Consultar stock actualizado
    // await obtenerStock(PRODUCTO_ID);

    console.log('\n✅ Operaciones completadas exitosamente');

  } catch (error) {
    console.error('\n❌ Error en la ejecución:', error.message);
    process.exit(1);
  }
}

// Ejecutar script
if (require.main === module) {
  main();
}

// Exportar funciones para uso en otros módulos
module.exports = {
  obtenerStock,
  ajustarStock,
  establecerStock
};
