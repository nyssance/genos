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

package com.example.genos

import android.content.Intent
import genos.extension.routes

fun router() { // singleTop的情况下标签切换无法起作用
    val home = "intent://home#Intent;scheme=genos-sample;package=com.example.genos;end"
    val flag = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
    routes = setOf(
        "home" to Intent.parseUri(home, Intent.URI_INTENT_SCHEME).apply {
            flags = flag
        },
        "discover" to Intent.parseUri(home, Intent.URI_INTENT_SCHEME).apply {
            flags = flag
        },
        "message" to Intent.parseUri(home, Intent.URI_INTENT_SCHEME).apply {
            flags = flag
        },
        "me" to Intent.parseUri(home, Intent.URI_INTENT_SCHEME).apply {
            flags = flag
        }
    )
}
