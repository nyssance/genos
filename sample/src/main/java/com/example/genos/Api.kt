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

import com.example.genos.model.User
import genos.vendor.retrofit
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://api.github.com"
val API: Api = retrofit(BASE_URL).create(Api::class.java)

interface Api {
    @GET("repos/square/retrofit/contributors")
    fun home(@Query("page") page: Int): Call<List<User>>

    @GET("repos/square/retrofit/contributors")
    fun userList(@Query("page") page: Int): Call<List<User>>

    @GET("users/{username}")
    fun userDetail(@Path("username") username: String): Call<User>
}
