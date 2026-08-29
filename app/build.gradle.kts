plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.legacy.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.example.game"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.game"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    //noinspection WrongGradleMethod
    kotlin {
        jvmToolchain(21)
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.preview)
    implementation(libs.compose.runtime)

    // Navigation
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Data Store
    implementation(libs.androidx.datastore.preferences)

    // Lifecycle + ViewModel
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel)

    // Hilt
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Image Loading
    implementation(libs.coil.compose)

    // Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)

    // Logging-Interceptor
    implementation(libs.logging.interceptor)

    implementation(libs.lottie.compose)


}