/*
 * Copyright 2018 NY (nyssance@icloud.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package genos;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseAppManager {
    //
    protected static String DOMAIN_NAME = "必填";
    protected static String API_VERSION = "api/v1";
    protected static String BASE_URL = "http://www." + DOMAIN_NAME;
    protected static String MEDIA_URL = "http://media." + DOMAIN_NAME;
    // Headers
    @NonNull
    protected static String APP_ID;
    protected static String APP_SCHEME = "local";
    protected static String APP_VERSION = "";
    protected static String AUTH_HEADER = "Authorization";
    protected static String AUTH_PREFIX = "JWT"; // Bearer
    protected static String TOKEN = "";

    public static String PLACE_CODE = "";
    //
    public static int LIST_START_PAGE = 1;

    public BaseAppManager() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(2)
                .tag("Genos")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
//        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
//            @Override
//            public boolean isLoggable(int priority, String tag) {
//                return BuildConfig.DEBUG;
//            }
//        });
        settings();
    }

    public abstract void settings();

    public abstract void route(Fragment fragment, String uri);

    protected Retrofit onCreateRetrofit() {
        long SIZE_OF_CACHE = 10 * 1024 * 1024; // 10 MiB
        // Cache cache = new Cache(new File("/data/user/0/com.nyssance.android/cache", "http"), SIZE_OF_CACHE);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                // OkHttp自动增加了Content-Length和Content-Type
                Request.Builder builder = chain.request().newBuilder()
                        .header("Accept-Language", Locale.getDefault().toString().replace("_", "-"))
                        .header("App-Scheme", APP_SCHEME);
                if (!TOKEN.isEmpty()) {
                    builder.header(AUTH_HEADER, String.format("%s %s", AUTH_PREFIX, TOKEN));
                }
                Request request = builder.build();
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
                    return chain.proceed(request);
                }
                Request compressedRequest = request.newBuilder()
                        .header("Content-Encoding", "gzip")
                        .method(request.method(), gzip(request.body()))
                        .build();
                return chain.proceed(compressedRequest);
            }

            private RequestBody gzip(final RequestBody body) {
                return new RequestBody() {
                    @Override
                    public MediaType contentType() {
                        return body.contentType();
                    }

                    @Override
                    public long contentLength() {
                        return -1; // We don't know the compressed length in advance!
                    }

                    @Override
                    public void writeTo(BufferedSink sink) throws IOException {
                        BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                        body.writeTo(gzipSink);
                        gzipSink.close();
                    }
                };
            }
        }).build();
        //      .cache(cache)

        //
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create(); // TODO: 是否必须
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .callFactory(httpClient) // 官方建议使用callFactory而不使用client方法
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
