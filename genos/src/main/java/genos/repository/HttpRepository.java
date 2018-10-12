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

package genos.repository;

import android.net.Uri;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import genos.BuildConfig;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpRepository<D> implements IRepository {
    // SO: https://stackoverflow.com/questions/45759063/android-livedata-observer-not-active-after-first-update
    @Override
    public MutableLiveData getData(@NonNull Call call, @NonNull MutableLiveData data) { // SO: https://stackoverflow.com/questions/35093884/retrofit-illegalstateexception-already-executed
        // (call.isExecuted() ? call.clone() : call).execute().body();
        call.enqueue(new Callback<D>() {
            @Override
            public void onResponse(@NonNull Call<D> call, @NonNull Response<D> response) {
                Request request = call.request();
                String scheme = request.url().scheme();
                String log = String.format("%s %s %s %s",
                        request.method(), getUrlString(request), response.code(), response.message());
                if (BuildConfig.DEBUG) {
                    log = String.format("%s\n\n▼ Response Headers\n%s\n▼ Request Headers\n%s", log, response.headers(), request.headers());
                }
                if (response.isSuccessful()) {
                    Logger.t(scheme).d("✅ " + log);
                    data.postValue(response.body());
                } else { // TODO: 处理detail
                    try {
                        Logger.t(scheme).d(String.format("❎ %s\n%s", log, response.errorBody().string()));
                    } catch (IOException e) {
                        Logger.t(scheme).e(scheme + e.getLocalizedMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<D> call, @NonNull Throwable t) {
                Request request = call.request();
                Logger.t(request.url().scheme()).e(t, String.format("❌ %s\n", getUrlString(request)));
            }

            private String getUrlString(@NonNull Request request) {
                return Uri.decode(request.url().toString());
            }
        });
        return data;
    }
}
