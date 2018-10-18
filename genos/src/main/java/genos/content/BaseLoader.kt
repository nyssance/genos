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

import androidx.loader.content.AsyncTaskLoader

abstract class BaseLoader<D>(context: Context) : AsyncTaskLoader<D>(context) {
    protected var mData: D? = null

    override fun deliverResult(data: D?) {
        if (isReset) {
            return
        }
        mData = data
        if (isStarted) {
            super.deliverResult(data)
        }
    }

    override fun onStartLoading() {
        if (mData != null) {
            deliverResult(mData)
        }
        if (takeContentChanged() || mData == null) {
            forceLoad()
        }
    }

    override fun onStopLoading() {
        cancelLoad()
    }

    override fun onReset() {
        super.onReset()
        onStopLoading()
        mData = null
    }
}
