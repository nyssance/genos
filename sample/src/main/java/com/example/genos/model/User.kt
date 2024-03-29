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

package com.example.genos.model

import com.google.gson.annotations.SerializedName
import genos.Global.APP_SCHEME

data class User(
    val id: Long,
    @SerializedName("login") val username: String,
    @SerializedName("avatar_url") val avatarUrl: String
) {
    companion object {
        val listLink = "$APP_SCHEME://users"
    }

    val link: String
        get() = "$listLink/$username"
}
