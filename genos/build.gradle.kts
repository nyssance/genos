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

//import com.novoda.gradle.release.PublishExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
//    id("com.novoda.bintray-release")
}

//configure<PublishExtension> {
//    userOrg = "nyssance"
//    groupId = "com.nyssance.genos"
//    artifactId = "genos"
//    publishVersion = "2.0.0"
//    desc = "The BEST high-level framework for Android by NY."
//    website = "https://github.com/nyssance/genos"
//}
kapt {
    useBuildCache = true
    javacOptions {
        option("-Xmaxerrs", 500)
    }
}

android {
    buildToolsVersion = "30.0.2"
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 200
        versionName = "2.0.0"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    val coreKTXVersion = "1.5.0-alpha02"
    val activityKTXVersion = "1.1.0"
    val fragmentKTXVersion = "1.2.5"
    val lifecycleKTXVersion = "2.2.0"
    val composeVersion = "0.1.0-dev17"

    val preferenceVersion = "1.1.1"
    val materialVersion = "1.3.0-alpha02"
    val recyclerviewSelectionVersion = "1.1.0-rc01"
    val swipeRefreshVersion = "1.1.0"
    val constraintVersion = "2.0.1"
    // https://developer.android.com/topic/libraries/architecture/adding-components
    val pagingVersion = "2.1.2"
    val workVersion = "1.0.1"
    // Vendor
    val glideVersion = "4.11.0" // https://github.com/bumptech/glide
    val loggerVersion = "2.2.0" // https://github.com/orhanobut/logger
    val retrofitVersion = "2.9.0" // https://square.github.io/retrofit/
    val agentWebVersion = "4.1.3" // https://github.com/Justson/AgentWeb

    // Kotlin
    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))
    // Android KTX https://developer.android.com/kotlin/ktx
    api("androidx.core:core-ktx:$coreKTXVersion")
    api("androidx.activity:activity-ktx:$activityKTXVersion")
    api("androidx.fragment:fragment-ktx:$fragmentKTXVersion")
    api("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleKTXVersion")
    // Jetpack Compose https://developer.android.com/jetpack/compose/
    api("androidx.compose.runtime:runtime:$composeVersion")
    api("androidx.compose.runtime:runtime-livedata:$composeVersion")
    api("androidx.compose.animation:animation:$composeVersion")
    api("androidx.compose.foundation:foundation:$composeVersion")
    api("androidx.compose.material:material:$composeVersion")
    api("androidx.compose.material:material-icons-extended:$composeVersion")
    api("androidx.compose.ui:ui:$composeVersion")
    api("androidx.ui:ui-tooling:$composeVersion")
    //
    api("androidx.preference:preference:$preferenceVersion")
    // Material Components for Android https://material.io/develop/android/
    api("com.google.android.material:material:$materialVersion")
    // https://developer.android.com/jetpack/androidx/versions
    api("androidx.recyclerview:recyclerview-selection:$recyclerviewSelectionVersion")
    api("androidx.swiperefreshlayout:swiperefreshlayout:$swipeRefreshVersion")
    api("androidx.constraintlayout:constraintlayout:$constraintVersion")
    // Paging, Work
    api("androidx.paging:paging-runtime:$pagingVersion")
    api("android.arch.work:work-runtime-ktx:$workVersion")
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
    api("com.just.agentweb:agentweb:$agentWebVersion")
    api("com.just.agentweb:filechooser:$agentWebVersion")
    api("com.download.library:Downloader:$agentWebVersion")
    // Test https://developer.android.com/training/testing/set-up-project#gradle-dependencies
    androidTestImplementation("androidx.test:rules:1.2.0")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.ui:ui-test:$composeVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}
