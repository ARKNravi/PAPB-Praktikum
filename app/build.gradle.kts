plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp") version "1.9.0-1.0.11"
}

android {
    namespace = "com.tifd.projectcomposed"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tifd.projectcomposed"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)

    // OkHttp and Coil
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.coil.compose)
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("io.coil-kt:coil-compose:1.3.2")

    // Compose and Material Icons
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.material3:material3:1.0.0")
    implementation("androidx.activity:activity-compose:1.6.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
    implementation("androidx.compose.foundation:foundation:1.5.0")
    implementation("androidx.compose.material:material-icons-extended:1.0.0")

    // Navigation for Compose
    implementation("androidx.navigation:navigation-compose:2.6.0")

    // Preview support
    implementation("androidx.compose.ui:ui-tooling-preview:1.0.0")

    // Room dependencies
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    // CameraX dependencies
    val cameraxVersion = "1.2.0-alpha01"
    // CameraX dependencies
    implementation("androidx.camera:camera-core:$cameraxVersion")
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")  // Updated to 1.2.0-alpha01
    implementation("androidx.camera:camera-extensions:$cameraxVersion")

    // FileProvider support
    implementation("androidx.core:core-ktx:1.6.0")
    ksp("androidx.room:room-compiler:2.5.2")
}