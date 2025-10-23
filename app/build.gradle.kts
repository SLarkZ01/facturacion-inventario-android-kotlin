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

    // Compose (usar catálogo de versiones para mantener consistencia)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    // Navigation Compose
    // Use literal notation for navigation-compose to avoid generated catalog accessor issues
    implementation("androidx.navigation:navigation-compose:2.9.4")
    // Se eliminó dependencia a Material3 para mantener compatibilidad con Compose 1.9.0 y usar Material (v1)
    // implementation(libs.androidx.compose.material3)
    // tooling for debug/inspection
    // Usar referencia del catálogo para la tooling library
    debugImplementation(libs.androidx.compose.ui.tooling)

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

    implementation(libs.androidx.cardview)

    // Google Sign-In (Play Services Auth)
    implementation(libs.google.play.services.auth)

    // Facebook Login (usar mavenCentral, catálogo de versiones)
    implementation(libs.facebook.login)
}