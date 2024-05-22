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
    id("com.android.library")
    id("maven-publish")
    kotlin("android")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.nyssance.genos"
                artifactId = "genos"
                version = "2.0.2"
            }
        }
    }
}

android {
    namespace = "com.nyssance.genos"
    compileSdk = 34
    defaultConfig {
        minSdk = 30
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures.viewBinding = true
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // https://developer.android.com/jetpack/androidx/versions
    val recyclerviewSelectionVersion = "1.1.0"
    val swipeRefreshVersion = "1.1.0"
    // Vendor
    val glideVersion = "4.16.0" // https://github.com/bumptech/glide
    val loggerVersion = "2.2.0" // https://github.com/orhanobut/logger
    val retrofitVersion = "2.11.0" // https://square.github.io/retrofit/
    val agentWebVersion = "v5.0.6-androidx" // https://github.com/Justson/AgentWeb

    // Kotlin
    api("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")
    api("org.jetbrains.kotlin:kotlin-reflect:2.0.0")
    // Android KTX https://developer.android.com/kotlin/ktx
    api("androidx.core:core-ktx:1.13.1")
    api("androidx.appcompat:appcompat:1.6.1")
    api("androidx.activity:activity-ktx:1.9.0")
    api("androidx.fragment:fragment-ktx:1.7.1")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    //
    api("androidx.preference:preference:1.2.1")
    // Material Components for Android https://m3.material.io
    api("com.google.android.material:material:1.12.0")
    api("androidx.recyclerview:recyclerview-selection:$recyclerviewSelectionVersion")
    api("androidx.swiperefreshlayout:swiperefreshlayout:$swipeRefreshVersion")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    // Paging, Work <https://developer.android.com/topic/libraries/architecture/adding-components>
    api("androidx.paging:paging-runtime:3.3.0")
    api("androidx.work:work-runtime-ktx:2.9.0")
    // Vendor
    api("com.github.bumptech.glide:glide:$glideVersion")
    annotationProcessor("com.github.bumptech.glide:compiler:$glideVersion")
    api("com.github.bumptech.glide:okhttp3-integration:$glideVersion")
    api("com.github.bumptech.glide:recyclerview-integration:$glideVersion") {
        isTransitive = false
    }
    api("com.orhanobut:logger:$loggerVersion")
    api("com.squareup.retrofit2:retrofit:$retrofitVersion")
    api("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    api("com.github.Justson.AgentWeb:agentweb-core:$agentWebVersion")
    api("com.github.Justson.AgentWeb:agentweb-filechooser:$agentWebVersion")
    api("com.github.Justson:Downloader:v5.0.4-androidx")
    // Test https://developer.android.com/training/testing/set-up-project#gradle-dependencies
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
}
