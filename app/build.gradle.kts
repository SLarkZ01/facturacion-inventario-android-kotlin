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
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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