/*
 * Copyright 2018 NY <nyssance@icloud.com>
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

import com.novoda.gradle.release.PublishExtension
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.novoda.bintray-release")
}

configure<PublishExtension> {
    userOrg = "nyssance"
    groupId = "com.nyssance.genos"
    artifactId = "genos"
    publishVersion = "1.1.8"
    desc = "The BEST high-level framework for Android by NY."
    website = "https://github.com/nyssance/genos"
}

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 118
        versionName = "1.1.8"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    useLibrary("android.test.runner")
}

dependencies {
    val ktxVersion = "1.2.0-alpha03"
    val ktxFragmentVersion = "1.2.0-alpha02"

    val materialVersion = "1.1.0-alpha09"
    val recyclerviewSelectionVersion = "1.1.0-alpha06"
    val swipeRefreshVersion = "1.1.0-alpha02"
    val constraintVersion = "2.0.0-beta2"
    // https://developer.android.com/topic/libraries/architecture/adding-components
    val pagingVersion = "2.1.0"
    val workVersion = "1.0.1"
    // Vendor
    val loggerVersion = "2.2.0"                 // https://github.com/orhanobut/logger
    val retrofitVersion = "2.6.1"               // https://square.github.io/retrofit/
    val glideVersion = "4.9.0"                  // https://github.com/bumptech/glide
    val eventbusVersion = "3.1.1"               // https://github.com/greenrobot/EventBus
    val agentWebVersion = "4.1.2"               // https://github.com/Justson/AgentWeb

    // Kotlin
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))
    implementation(kotlin("reflect", KotlinCompilerVersion.VERSION))
    // Material Components for Android https://material.io/develop/android/
    api("com.google.android.material:material:$materialVersion")
    // Android KTX https://developer.android.com/kotlin/ktx
    api("androidx.core:core-ktx:$ktxVersion")
    api("androidx.fragment:fragment-ktx:$ktxFragmentVersion")
    // https://developer.android.com/jetpack/androidx/versions
    api("androidx.recyclerview:recyclerview-selection:$recyclerviewSelectionVersion")
    api("androidx.swiperefreshlayout:swiperefreshlayout:$swipeRefreshVersion")
    api("androidx.constraintlayout:constraintlayout:$constraintVersion")
    // Paging, Work
    api("androidx.paging:paging-runtime:$pagingVersion")
    api("android.arch.work:work-runtime-ktx:$workVersion")
    // Vendor
    api("com.orhanobut:logger:$loggerVersion")
    api("com.squareup.retrofit2:retrofit:$retrofitVersion")
    api("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    api("com.github.bumptech.glide:glide:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")
    implementation("com.github.bumptech.glide:okhttp3-integration:$glideVersion")
    api("com.github.bumptech.glide:recyclerview-integration:$glideVersion") {
        isTransitive = false
    }
    api("org.greenrobot:eventbus:$eventbusVersion")
    api("com.just.agentweb:agentweb:$agentWebVersion")
    api("com.just.agentweb:filechooser:$agentWebVersion")
    api("com.download.library:Downloader:$agentWebVersion")
    // Test
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
