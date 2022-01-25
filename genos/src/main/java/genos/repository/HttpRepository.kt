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

import androidx.lifecycle.MutableLiveData
import genos.repository.HttpUtil.request
import retrofit2.Call

class HttpRepository<D : Any> : IRepository<D> {
    override fun getData(call: Call<out D>, data: MutableLiveData<D>, success: (Int) -> Unit, failure: (Int, String) -> Unit): MutableLiveData<D> {
        request(call, { status, response ->
            data.postValue(response.body())
            success(status)
        }, failure)
        return data
    }
}
