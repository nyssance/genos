/*
 * Copyright 2020 NY <nyssance@icloud.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("com.android.application")
    id("kotlin-parcelize")
    kotlin("android")
}

android {
    namespace = "com.example.genos"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.example.genos"
        minSdk = 28
        targetSdk = 33
        versionCode = 202
        versionName = "2.0.2"
        vectorDrawables.useSupportLibrary = true
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
    buildFeatures {
        viewBinding = true
        compose = true
    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
    composeOptions.kotlinCompilerExtensionVersion = "1.4.2"
    kotlin {
        jvmToolchain(17)
    }
//    kotlinOptions {
//        jvmTarget = "17"
////        useFir = true
//    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.example.genos"
}

dependencies {
    implementation(project(":genos"))
    // Jetpack Compose https://developer.android.com/jetpack/compose/setup
    val composeBom = platform("androidx.compose:compose-bom:2023.01.00")
    val composeTestVersion = "1.3.3"
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.material3:material3:1.0.1")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview:$composeTestVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeTestVersion")
    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeTestVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeTestVersion")
    // Optional
    implementation("androidx.activity:activity-compose:1.6.1")
    // Test
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
}

//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
//    kotlinOptions {
//        jvmTarget = JavaVersion.VERSION_17.toString()
//        freeCompilerArgs = freeCompilerArgs + "-Xskip-prerelease-check"
//    }
//}
