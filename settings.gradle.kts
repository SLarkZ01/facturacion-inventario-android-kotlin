pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        // Mantener estos repositorios por si se requiere resolver artefactos relacionados con Compose
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/compose-compiler")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/compose")
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/compose-compiler")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/compose")
    }
}

rootProject.name = "Facturacion-inventario"
include(":app")
