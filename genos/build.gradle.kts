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

import org.jetbrains.kotlin.config.KotlinCompilerVersion
import com.novoda.gradle.release.PublishExtension

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
    publishVersion = "1.1.4"
    desc = "The BEST high-level framework for Android by NY."
    website = "https://github.com/nyssance/genos"
}

android {
    compileSdkVersion(28)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 114
        versionName = "1.1.4"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    val ktxCoreVersion = "1.0.1"
    val ktxFragmentVersion = "1.0.0"

    val materialVersion = "1.0.0"
    // https://developer.android.com/topic/libraries/support-library/features.html
    val constraintVersion = "2.0.0-alpha2"
    val lifecycleVersion = "2.0.0"
    // https://developer.android.com/topic/libraries/architecture/adding-components.html
    val pagingVersion = "2.0.0"
    val workVersion = "1.0.0-alpha11"
    // vendor
    val loggerVersion = "2.2.0"                 // https://github.com/orhanobut/logger
    val retrofitVersion = "2.4.0"               // https://square.github.io/retrofit/
    val glideVersion = "4.8.0"                  // https://github.com/bumptech/glide
    val eventbusVersion = "3.1.1"               // https://github.com/greenrobot/EventBus

    // Kotlin
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))
    // KTX
    api("androidx.core:core-ktx:$ktxCoreVersion")
    api("androidx.fragment:fragment-ktx:$ktxFragmentVersion")
    //
    api("com.google.android.material:material:$materialVersion")
    api("androidx.cardview:cardview:$materialVersion")
    api("androidx.recyclerview:recyclerview-selection:$materialVersion")
    api("androidx.constraintlayout:constraintlayout:$constraintVersion")
    // ViewModel and LiveData
    api("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    // Java8 support for Lifecycle
    api("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    // Paging, Work
    api("androidx.paging:paging-runtime:$pagingVersion")
    api("android.arch.work:work-runtime-ktx:$workVersion")
    // Test helpers for LiveData
    testImplementation("androidx.arch.core:core-testing:$lifecycleVersion")

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
    // Test
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.0.0")
    androidTestImplementation("androidx.test:runner:1.1.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0")
}
