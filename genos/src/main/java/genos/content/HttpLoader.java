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

package genos.content;

import android.content.Context;
import android.net.Uri;

import com.orhanobut.logger.Logger;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;

public class HttpLoader<D> extends BaseLoader<D> {
    private Call<D> mCall;

    public HttpLoader(Context context, Call<D> call) {
        super(context);
        mCall = call;
    }

    @Override
    public final D loadInBackground() {
        try { // SO: http://stackoverflow.com/questions/35093884/retrofit-illegalstateexception-already-executed
            // mCall.isExecuted() ? mCall.clone() : mCall).execute().body();
            Request request = mCall.request();
            Response<D> response = (mCall.isExecuted() ? mCall.clone() : mCall).execute();
            int code = response.code();
            Logger.t("http").d(String.format("%s %s %s %s\n\n▼ Response Headers\n%s\n▼ Request Headers\n%s",
                    request.method(), Uri.decode(request.url().toString()), code, response.message(),
                    response.headers(),
                    request.headers()));
            if (response.isSuccessful()) {
                return response.body();
            } else {
                return response.body(); // TODO: 处理detail
            }
        } catch (Exception e) {
            Logger.t("http").e(e, "Exception");
        }
        return null;
    }
}
