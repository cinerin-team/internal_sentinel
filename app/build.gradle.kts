plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.internal_sentinel"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.internal_sentinel"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.monitor)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit.junit)
}
