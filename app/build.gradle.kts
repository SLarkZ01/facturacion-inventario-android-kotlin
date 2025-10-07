plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.facturacion_inventario"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.facturacion_inventario"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Client ID para Google (reemplazado por el valor proporcionado)
        buildConfigField("String", "GOOGLE_SERVER_CLIENT_ID", "\"926718099758-osi38sedrsjvil21a10f1eq25ub0q351.apps.googleusercontent.com\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Habilitar generación de BuildConfig (requerido si usas buildConfigField)
    buildFeatures {
        buildConfig = true
        // Habilitar Jetpack Compose
        compose = true
    }

    composeOptions {
        // Usar la versión del compilador establecida explícitamente para evitar problemas con el catálogo
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        // Mantener opciones adicionales si es necesario
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose
    implementation("androidx.compose.ui:ui:1.9.0")
    implementation("androidx.compose.material:material:1.9.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.9.0")
    implementation("androidx.activity:activity-compose:1.10.1")
    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.9.4")
    // Material icons (core + extended) necesarias para usar Icons.Default
    // Las dependencias de material-icons se han eliminado porque los iconos usan drawables (painterResource)
    // tooling for debug/inspection
    debugImplementation("androidx.compose.ui:ui-tooling:1.9.0")

    // Networking & JSON (usar catálogo de versiones)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Gson + Retrofit converter (catalog)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)

    // Coroutines & lifecycle (catalog)
    implementation(libs.coroutines.android)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.material)
    implementation(libs.androidx.cardview)

    // Google Sign-In (Play Services Auth)
    implementation(libs.google.play.services.auth)

    // Facebook Login (usar mavenCentral, catálogo de versiones)
    implementation(libs.facebook.login)
}