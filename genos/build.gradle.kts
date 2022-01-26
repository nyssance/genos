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
    kotlin("android")
    kotlin("kapt")
    id("maven-publish")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.nyssance.genos"
                artifactId = "genos"
                version = "2.0.0-alpha07"
            }
        }
    }
}

android {
    compileSdk = 32
    defaultConfig {
        minSdk = 27
        targetSdk = 32
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures.viewBinding = true
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // https://developer.android.com/jetpack/androidx/versions
    val activityKTXVersion = "1.4.0"
    val fragmentKTXVersion = "1.4.0"
    val recyclerviewSelectionVersion = "1.1.0"
    val swipeRefreshVersion = "1.1.0"
    // Vendor
    val glideVersion = "4.12.0" // https://github.com/bumptech/glide
    val loggerVersion = "2.2.0" // https://github.com/orhanobut/logger
    val retrofitVersion = "2.9.0" // https://square.github.io/retrofit/
    val agentWebVersion = "v5.0.0-alpha.1-androidx" // https://github.com/Justson/AgentWeb

    // Kotlin
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")
    api("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    // Android KTX https://developer.android.com/kotlin/ktx
    api("androidx.core:core-ktx:1.7.0")
    api("androidx.appcompat:appcompat:1.4.1")
    api("androidx.activity:activity-ktx:$activityKTXVersion")
    api("androidx.fragment:fragment-ktx:$fragmentKTXVersion")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    //
    api("androidx.preference:preference:1.1.1")
    // Material Components for Android https://material.io/develop/android/
    api("com.google.android.material:material:1.5.0")
    api("androidx.recyclerview:recyclerview-selection:$recyclerviewSelectionVersion")
    api("androidx.swiperefreshlayout:swiperefreshlayout:$swipeRefreshVersion")
    api("androidx.constraintlayout:constraintlayout:2.1.3")
    // Paging, Work <https://developer.android.com/topic/libraries/architecture/adding-components>
    api("androidx.paging:paging-runtime:3.1.0")
    api("androidx.work:work-runtime-ktx:2.7.1")
    // Vendor
    api("com.github.bumptech.glide:glide:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")
    api("com.github.bumptech.glide:okhttp3-integration:$glideVersion")
    api("com.github.bumptech.glide:recyclerview-integration:$glideVersion") {
        isTransitive = false
    }
    api("com.orhanobut:logger:$loggerVersion")
    api("com.squareup.retrofit2:retrofit:$retrofitVersion")
    api("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    api("com.github.Justson.AgentWeb:agentweb-core:$agentWebVersion")
    api("com.github.Justson.AgentWeb:agentweb-filechooser:$agentWebVersion")
    api("com.github.Justson:Downloader:v5.0.0-androidx")
    // Test https://developer.android.com/training/testing/set-up-project#gradle-dependencies
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}
