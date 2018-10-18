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

package genos

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

import java.io.IOException
import java.util.Locale
import androidx.fragment.app.Fragment
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.BufferedSink
import okio.GzipSink
import okio.Okio
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseAppManager {
    init {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(2)
                .tag("Genos")
                .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        //        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
        //            @Override
        //            public boolean isLoggable(int priority, String tag) {
        //                return BuildConfig.DEBUG;
        //            }
        //        });
        settings()
    }

    abstract fun settings()

    abstract fun route(fragment: Fragment, uri: String)

    protected fun onCreateRetrofit(): Retrofit {
        val SIZE_OF_CACHE = (10 * 1024 * 1024).toLong() // 10 MiB
        // Cache cache = new Cache(new File("/data/user/0/com.nyssance.android/cache", "http"), SIZE_OF_CACHE);
        val httpClient = OkHttpClient.Builder().addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                // OkHttp自动增加了Content-Length和Content-Type
                val builder = chain.request().newBuilder()
                        .header("Accept-Language", Locale.getDefault().toString().replace("_", "-"))
                        .header("App-Scheme", APP_SCHEME)
                if (!AUTH_TOKEN.isEmpty()) {
                    builder.header(AUTH_HEADER, String.format("%s %s", AUTH_PREFIX, AUTH_TOKEN))
                }
                val request = builder.build()
                // 发送数据的时候GZip压缩 https://github.com/square/okhttp/wiki/Interceptors
                if (request.body() == null || request.header("Content-Encoding") != null) {
                    //                    Response response = chain.proceed(request);
                    //                    String cacheControl = request.cacheControl().toString();
                    //                    if (TextUtils.isEmpty(cacheControl)) {
                    //                        cacheControl = "public, max-age=60";
                    //                    }
                    //                    return response.newBuilder()
                    //                            .header("Cache-Control", cacheControl)
                    //                            .removeHeader("Pragma")
                    //                            .build();
                    return chain.proceed(request)
                }
                val compressedRequest = request.newBuilder()
                        .header("Content-Encoding", "gzip")
                        .method(request.method(), gzip(request.body()))
                        .build()
                return chain.proceed(compressedRequest)
            }

            private fun gzip(body: RequestBody?): RequestBody {
                return object : RequestBody() {
                    override fun contentType(): MediaType? {
                        return body?.contentType()
                    }

                    override fun contentLength(): Long {
                        return -1 // We don't know the compressed length in advance!
                    }

                    @Throws(IOException::class)
                    override fun writeTo(sink: BufferedSink) {
                        val gzipSink = Okio.buffer(GzipSink(sink))
                        body?.writeTo(gzipSink)
                        gzipSink.close()
                    }
                }
            }
        }).build()
        //      .cache(cache)

        //
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create() // TODO: 是否必须
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .callFactory(httpClient) // 官方建议使用callFactory而不使用client方法
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    companion object {
        //
        var LIST_START_PAGE = 1
        //
        protected var BASE_URL = "https://www.必填.com"
        protected var APP_SCHEME = "genos"
        protected var AUTH_HEADER = "Authorization"
        protected var AUTH_PREFIX = "JWT" // JWT / Bearer
        protected var AUTH_TOKEN = ""
    }
}
