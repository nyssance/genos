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

package genos

import android.content.Context
import android.os.Environment.MEDIA_MOUNTED
import android.os.Environment.MEDIA_MOUNTED_READ_ONLY
import android.os.Environment.getExternalStorageState
import com.orhanobut.logger.Logger

// NY - ContextCompat, ViewCompat里有很多版本兼容方法, 不用自己写

object Helper {
    // Android https://developer.android.com/guide/topics/data/data-storage#filesExternal
    val isExternalStorageReadable by lazy { setOf(MEDIA_MOUNTED, MEDIA_MOUNTED_READ_ONLY).contains(getExternalStorageState()) }

    val isExternalStorageWritable by lazy { MEDIA_MOUNTED == getExternalStorageState() }

    fun getApplicationName(context: Context): String {
        val info = context.applicationInfo
        val resId = info.labelRes
        return if (resId > 0) context.getString(resId) else info.nonLocalizedLabel.toString()
    }

    fun getResId(context: Context, name: String, defType: String, log: Boolean = true): Int {
        val resId = context.resources.getIdentifier(name, defType, context.packageName)
        if (log && resId == 0) {
            Logger.t(this::class.simpleName).wtf("R.$defType.$name 不存在")
        }
        return resId
    }
}
