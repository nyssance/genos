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

package genos.vendor

import com.google.gson.GsonBuilder
import genos.Global.APP_SCHEME
import genos.Global.AUTH_HEADER
import genos.Global.AUTH_PREFIX
import genos.Global.AUTH_TOKEN
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import okio.BufferedSink
import okio.GzipSink
import okio.Okio
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*

fun retrofit(
    baseUrl: String,
    converter: Converter.Factory = GsonConverterFactory.create(
        GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()
    ),
    interceptor: Interceptor = object : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            // OkHttp自动增加了Content-Length和Content-Type
            val builder = chain.request().newBuilder()
                .header("Accept-Language", Locale.getDefault().toString().replace("_", "-"))
                .header("App-Scheme", APP_SCHEME)
            if (AUTH_TOKEN.isNotBlank()) {
                builder.header(AUTH_HEADER, "$AUTH_PREFIX $AUTH_TOKEN")
            }
            val request = builder.build()
            // 发送数据的时候GZip压缩 https://square.github.io/okhttp/interceptors
            if (request.body() == null || !request.header("Content-Encoding").isNullOrBlank()) {
//                                        val response = chain.proceed(request)
//                                        val cacheControl = request.cacheControl().toString()
//                                        cacheControl.ifBlank {
//                                            "public, max-age=60"
//                                        }
//                                        return response.newBuilder()
//                                                .header("Cache-Control", cacheControl)
//                                                .removeHeader("Pragma")
//                                                .build()
                return chain.proceed(request)
            }
            val compressedRequest = request.newBuilder()
                .header("Content-Encoding", "gzip")
                .method(request.method(), gzip(request.body()!!))
                .build()
            return chain.proceed(compressedRequest)
        }

        private fun gzip(body: RequestBody) = object : RequestBody() {
            override fun contentType() = body.contentType()

            override fun contentLength() = -1L

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                val gzipSink = Okio.buffer(GzipSink(sink))
                body.writeTo(gzipSink)
                gzipSink.close()
            }
        }
    }
): Retrofit {
//    Environment.getExternalStorageState()
//     val SIZE_OF_CACHE = 10L * 1024 * 1024 // 10 MiB
//     val cache = Cache(File("/data/user/0/com.nyssance.android/cache", "http"), SIZE_OF_CACHE)
//    getExternalCacheDir()
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    //      .cache(cache)
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .callFactory(client) // 官方建议使用callFactory而不使用client方法
        .addConverterFactory(converter)
        .build()
}
