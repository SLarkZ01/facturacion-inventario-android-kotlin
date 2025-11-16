package com.example.facturacion_inventario.data.repository

import com.example.facturacion_inventario.domain.model.Product
import com.example.facturacion_inventario.domain.model.Category
import com.example.facturacion_inventario.domain.model.ProductMedia
import com.example.facturacion_inventario.domain.model.ProductSpecification
import com.example.facturacion_inventario.domain.model.MediaType
import com.example.facturacion_inventario.domain.repository.ProductRepository
import com.example.facturacion_inventario.R

class FakeProductRepository : ProductRepository {
    // CATEGORÍAS ELIMINADAS - Se consumen desde la API
    // Ya no se usan categorías fake locales
    private val categories = emptyList<Category>()

    private val items = listOf(
        // Motor
        Product(
            id = "m1",
            name = "Pistón con anillos 150cc",
            description = "Pistón completo con juego de anillos",
            categoryId = "motor",
            price = 45000.0,
            stock = 12,
            mediaList = listOf(
                ProductMedia(R.drawable.ic_engine, MediaType.IMAGE),
                ProductMedia(R.drawable.ic_motorcycle_animated, MediaType.IMAGE),
                ProductMedia(R.drawable.ic_gear, MediaType.IMAGE)
            ),
            specifications = listOf(
                ProductSpecification("Marca", "Yamaha Genuine Parts"),
                ProductSpecification("Cilindraje", "150cc"),
                ProductSpecification("Diámetro", "57mm"),
                ProductSpecification("Material", "Aluminio forjado"),
                ProductSpecification("Anillos incluidos", "Sí (compresión y rascador)"),
                ProductSpecification("Compatibilidad", "Yamaha YBR 150, FZ 150"),
                ProductSpecification("Peso", "280g"),
                ProductSpecification("Garantía", "6 meses")
            ),
            features = listOf(
                "Construcción en aluminio de alta resistencia",
                "Anillos de compresión de primera calidad",
                "Tratamiento térmico para mayor durabilidad",
                "Diseño optimizado para reducir fricción",
                "Compatible con motores 4 tiempos",
                "Instalación plug and play"
            )
        ),
        Product(
            id = "m2",
            name = "Bujía NGK estándar",
            description = "Bujía de alta performance",
            categoryId = "motor",
            price = 8500.0,
            stock = 50,
            mediaList = listOf(
                ProductMedia(R.drawable.ic_engine, MediaType.IMAGE),
                ProductMedia(R.drawable.ic_electric, MediaType.IMAGE)
            ),
            specifications = listOf(
                ProductSpecification("Marca", "NGK"),
                ProductSpecification("Modelo", "CR7HSA"),
                ProductSpecification("Tipo", "Estándar cobre"),
                ProductSpecification("Rosca", "10mm"),
                ProductSpecification("Alcance", "12.7mm"),
                ProductSpecification("Temperatura", "Rango medio"),
                ProductSpecification("Resistencia", "5k Ohm")
            ),
            features = listOf(
                "Electrodo central de cobre para mejor conductividad",
                "Resistencia incorporada para reducir interferencias",
                "Diseño anti-fouling para evitar carbonización",
                "Optimizada para arranque en frío",
                "Compatible con sistemas de inyección electrónica"
            )
        ),
        Product(id = "m3", name = "Filtro de aire deportivo", description = "Filtro de alto flujo reutilizable", categoryId = "motor", price = 35000.0, stock = 15),
        Product(id = "m4", name = "Válvulas admisión/escape", description = "Juego de válvulas completo", categoryId = "motor", price = 55000.0, stock = 8),

        // Transmisión
        Product(
            id = "t1",
            name = "Cadena de transmisión 428H",
            description = "Cadena reforzada 120 eslabones",
            categoryId = "transmision",
            price = 42000.0,
            stock = 20,
            mediaList = listOf(
                ProductMedia(R.drawable.ic_gear, MediaType.IMAGE),
                ProductMedia(R.drawable.ic_motorcycle_animated, MediaType.IMAGE),
                ProductMedia(R.drawable.ic_tools, MediaType.IMAGE)
            ),
            specifications = listOf(
                ProductSpecification("Marca", "DID Racing"),
                ProductSpecification("Tipo", "428H Heavy Duty"),
                ProductSpecification("Eslabones", "120 Links"),
                ProductSpecification("Paso", "12.7mm (1/2 pulgada)"),
                ProductSpecification("Material", "Acero cromado"),
                ProductSpecification("Resistencia", "2,800 lbs"),
                ProductSpecification("Tratamiento", "Cromado y sellado con O-Ring"),
                ProductSpecification("Color", "Plata/Negro")
            ),
            features = listOf(
                "O-Ring sellados para retención de lubricante",
                "Resistencia superior a cargas extremas",
                "Tratamiento anti-corrosión",
                "Acabado cromado de larga duración",
                "Compatible con la mayoría de motos 125-200cc",
                "Incluye eslabón maestro de unión"
            )
        ),
        Product(id = "t2", name = "Kit piñón y corona", description = "Juego completo relación 14-40", categoryId = "transmision", price = 68000.0, stock = 10),
        Product(id = "t3", name = "Kit de embrague completo", description = "Discos, muelles y separadores", categoryId = "transmision", price = 95000.0, stock = 7),

        // Frenos
        Product(
            id = "f1",
            name = "Pastillas freno delantero",
            description = "Alto agarre y durabilidad",
            categoryId = "frenos",
            price = 28000.0,
            stock = 30,
            mediaList = listOf(
                ProductMedia(R.drawable.ic_brake, MediaType.IMAGE),
                ProductMedia(R.drawable.ic_suspension, MediaType.IMAGE)
            ),
            specifications = listOf(
                ProductSpecification("Marca", "Brembo"),
                ProductSpecification("Código", "07BB33SA"),
                ProductSpecification("Material", "Compuesto semi-metálico"),
                ProductSpecification("Posición", "Pinza delantera"),
                ProductSpecification("Temperatura máx.", "650°C"),
                ProductSpecification("Grosor", "7.5mm"),
                ProductSpecification("Incluye", "2 pastillas + clips")
            ),
            features = listOf(
                "Compuesto de alto coeficiente de fricción",
                "Excelente rendimiento en condiciones húmedas",
                "Bajo desgaste del disco de freno",
                "Sin ruidos ni vibraciones",
                "Frenado progresivo y predecible",
                "Homologadas para uso en carretera"
            )
        ),
        Product(id = "f2", name = "Disco de freno 260mm", description = "Disco ventilado delantero", categoryId = "frenos", price = 85000.0, stock = 12),
        Product(id = "f3", name = "Líquido de frenos DOT4", description = "Botella 500ml", categoryId = "frenos", price = 12000.0, stock = 40),
        Product(id = "f4", name = "Bomba freno delantera", description = "Bomba hidráulica universal", categoryId = "frenos", price = 65000.0, stock = 8),

        // Suspensión
        Product(id = "s1", name = "Amortiguador trasero regulable", description = "Ajuste precarga y rebote", categoryId = "suspension", price = 125000.0, stock = 6),
        Product(id = "s2", name = "Rodamientos dirección", description = "Juego completo con retenes", categoryId = "suspension", price = 45000.0, stock = 15),
        Product(id = "s3", name = "Aceite telescópico 10W", description = "1 litro para horquillas", categoryId = "suspension", price = 18000.0, stock = 25),

        // Eléctrico
        Product(id = "e1", name = "Batería 12V 7Ah", description = "Batería gel libre mantenimiento", categoryId = "electrico", price = 85000.0, stock = 18),
        Product(id = "e2", name = "Kit luces LED", description = "Faros delanteros LED H4", categoryId = "electrico", price = 95000.0, stock = 12),
        Product(id = "e3", name = "CDI regulador", description = "CDI universal programable", categoryId = "electrico", price = 55000.0, stock = 10),
        Product(id = "e4", name = "Bobina de encendido", description = "Bobina alta tensión", categoryId = "electrico", price = 38000.0, stock = 14),

        // Carrocería
        Product(id = "c1", name = "Espejo retrovisor universal", description = "Par de espejos cromados", categoryId = "carroceria", price = 25000.0, stock = 35),
        Product(id = "c2", name = "Asiento confort", description = "Asiento ergonómico con gel", categoryId = "carroceria", price = 120000.0, stock = 8),
        Product(id = "c3", name = "Manubrios deportivos", description = "Manillar aluminio negro", categoryId = "carroceria", price = 78000.0, stock = 10),

        // Ruedas y Neumáticos
        Product(id = "r1", name = "Neumático trasero 120/70-17", description = "Llanta deportiva radial", categoryId = "ruedas", price = 185000.0, stock = 15),
        Product(id = "r2", name = "Neumático delantero 100/80-17", description = "Llanta con buen agarre", categoryId = "ruedas", price = 155000.0, stock = 18),
        Product(id = "r3", name = "Cámara 17 pulgadas", description = "Cámara reforzada", categoryId = "ruedas", price = 18000.0, stock = 30),

        // Accesorios
        Product(id = "a1", name = "Casco integral certificado", description = "Casco DOT talla L", categoryId = "accesorios", price = 165000.0, stock = 12),
        Product(id = "a2", name = "Guantes protección", description = "Guantes con nudilleras", categoryId = "accesorios", price = 45000.0, stock = 20),
        Product(id = "a3", name = "Maleta trasera 30L", description = "Baúl con respaldo", categoryId = "accesorios", price = 95000.0, stock = 8),

        // Lubricantes
        Product(id = "l1", name = "Aceite motor 10W-40 4T", description = "Semisintético 4 litros", categoryId = "lubricantes", price = 48000.0, stock = 40),
        Product(id = "l2", name = "Grasa cadena spray", description = "Lubricante cadena 400ml", categoryId = "lubricantes", price = 15000.0, stock = 50),
        Product(id = "l3", name = "Limpiador cadena", description = "Desengrasante 500ml", categoryId = "lubricantes", price = 12000.0, stock = 35),

        // Herramientas
        Product(id = "h1", name = "Kit llaves hexagonales", description = "Juego 8 piezas métricas", categoryId = "herramientas", price = 32000.0, stock = 15),
        Product(id = "h2", name = "Extractor volante", description = "Extractor universal", categoryId = "herramientas", price = 28000.0, stock = 10),
        Product(id = "h3", name = "Manómetro presión llantas", description = "Medidor digital", categoryId = "herramientas", price = 22000.0, stock = 18)
    )

    fun getCategories(): List<Category> = categories

    fun getProductsByCategory(categoryId: String): List<Product> =
        items.filter { it.categoryId == categoryId }

    override fun getProducts(): List<Product> = items

    override fun getProductById(id: String): Product? = items.find { it.id == id }
}
