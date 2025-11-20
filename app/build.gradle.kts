import java.util.Properties
import java.io.FileInputStream
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.kittel.ultraminimallauncher"
    compileSdk = 36

    val localPropertiesFile = rootProject.file("local.properties")
    val localProperties = Properties()
    if (localPropertiesFile.exists()) {
        localProperties.load(FileInputStream(localPropertiesFile))
    }

    signingConfigs {
        create("release") {
            if (localProperties.containsKey("keystore.file")) {
                storeFile = file(localProperties.getProperty("keystore.file"))
                storePassword = localProperties.getProperty("keystore.password")
                keyAlias = localProperties.getProperty("key.alias")
                keyPassword = localProperties.getProperty("key.password")
            }
        }
    }

    defaultConfig {
        applicationId = "com.kittel.ultraminimallauncher"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Vektor-Grafiken für Compose aktivieren
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    // NEU: Compose Build-Features aktivieren
    buildFeatures {
        compose = true
    }
    // NEU: Compose Compiler-Version festlegen
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Passende Version für Ihren Kotlin-Compiler
    }
    // NEU: Packaging-Optionen für Compose
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
dependencies {
    implementation("com.google.accompanist:accompanist-drawablepainter:0.32.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // NEU: Jetpack Compose-Bibliotheken
    implementation(platform("androidx.compose:compose-bom:2024.05.00")) // Die BoM (Bill of Materials) verwaltet die Versionen für Sie
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.0") // Integration mit Activity

    // Nur für die Vorschau in Android Studio benötigt
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-tooling-preview")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // NEU: Test-Bibliothek für Compose (optional, aber empfohlen)
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("com.google.code.gson:gson:2.10.1")
}