import java.util.Properties

// Create an object to read the local.properties file
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // ** REMOVED THIS LINE: id("com.google.gms.google-services") **
}

android {
// ... rest of your file is correct
    namespace = "com.example.kisanbandhu"
    // Use the latest stable SDK, 34
    compileSdk = 36

    buildFeatures {
        viewBinding = true
        // ** ADD THIS LINE - This was the cause of your error **
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.kisanbandhu"
        minSdk = 24
        // Target the latest stable SDK, 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // This correctly reads the key from local.properties
        val apiKey = localProperties.getProperty("API_KEY") ?: ""
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
    }

    buildTypes {
        debug {
            // The key from defaultConfig is automatically used
        }
        release {
            // ** REMOVED buildConfigField from here **
            // It's already defined in defaultConfig and will be used for release builds.
            // The old line was also pointing to the wrong place (System.getProperties)
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
    implementation(libs.androidx.activity) // <-- This is correct
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // ViewModel and Coroutines
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    // ** REMOVED DUPLICATE: implementation("androidx.activity:activity-ktx:1.9.0") **
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // **Google Gemini AI SDK**
    implementation("com.google.ai.client.generativeai:generativeai:0.7.0")

    // Image Loading (Coil)
    implementation("io.coil-kt:coil:2.6.0")
}