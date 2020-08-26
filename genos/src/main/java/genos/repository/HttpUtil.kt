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

package genos.repository

import android.net.Uri
import com.nyssance.genos.BuildConfig
import com.orhanobut.logger.Logger
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object HttpUtil {
    fun <D : Any> request(call: Call<D>, success: (Int, Response<D>) -> Unit, failure: (Int, String) -> Unit) {
        // SO https://stackoverflow.com/questions/35093884/retrofit-illegalstateexception-already-executed#35094488
        (if (call.isExecuted) call.clone() else call).enqueue(object : Callback<D> {
            override fun onResponse(call: Call<D>, response: Response<D>) {
                val request = call.request()
                val scheme = request.url().scheme()
                val status = response.code()
                val log = "${request.method()} ${getUrlString(request)} $status ${response.message()}\n\n"
                if (BuildConfig.DEBUG) {
                    log.plus("▼ Response Headers\n${response.headers()}\n▼ Request Headers\n${request.headers()}")
                }
                if (response.isSuccessful) {
                    Logger.t(scheme).d("✅ $log")
                    success(status, response)
                } else {
                    failure(status, response.errorBody()?.string().orEmpty())
                    try {
                        Logger.t(scheme).d("❎ $log\n${response.errorBody()?.string()}")
                    } catch (cause: Throwable) {
                        Logger.t(scheme).e(cause, "Throwable")
                    }
                }
            }

            override fun onFailure(call: Call<D>, throwable: Throwable) {
                failure(666, throwable.localizedMessage.orEmpty())
                val request = call.request()
                Logger.t(request.url().scheme()).e(throwable, "❌ ${getUrlString(request)}\n")
            }

            private fun getUrlString(request: Request) = Uri.decode(request.url().toString())
        })
    }
}
