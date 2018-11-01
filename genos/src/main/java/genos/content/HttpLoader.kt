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

package genos.content

import android.content.Context
import android.net.Uri
import com.orhanobut.logger.Logger
import retrofit2.Call

class HttpLoader<D>(context: Context, private val call: Call<D>) : BaseLoader<D>(context) {

    override fun loadInBackground(): D? {
        try { // SO: https://stackoverflow.com/questions/35093884/retrofit-illegalstateexception-already-executed
            // (if (call.isExecuted) call.clone() else call).execute().body()
            val request = call.request()
            val response = (if (call.isExecuted) call.clone() else call).execute()
            val code = response.code()
            Logger.t("http").d("${request.method()} ${Uri.decode(request.url().toString())} $code ${response.message()}\n\n▼ Response Headers\n${response.headers()}\n▼ Request Headers\n${request.headers()}")
            return if (response.isSuccessful) {
                response.body()
            } else {
                response.body() // TODO: 处理detail
            }
        } catch (e: Exception) {
            Logger.t("http").e(e, "Exception")
        }
        return null
    }
}
