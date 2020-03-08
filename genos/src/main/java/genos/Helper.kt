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

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import com.orhanobut.logger.Logger

// NY: ContextCompat, ViewCompat里有很多版本兼容方法, 不用自己写

object Helper {
    // Android https://developer.android.com/guide/topics/data/data-storage#filesExternal
    val isExternalStorageReadable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
        }

    val isExternalStorageWritable: Boolean
        get() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    private fun getAllResourceIDs(dataClass: Class<Any>): IntArray { // 传入class为.R
        val resourceIDFields = dataClass.fields
        val resourceIDs = IntArray(resourceIDFields.size)
        val count = resourceIDFields.size
        try { // pass 'null' because class is static
            repeat(count) {
                resourceIDs[it] = resourceIDFields[it].getInt(null)
            }
        } catch (cause: Throwable) {
            Logger.t(this::class.simpleName).e(cause, "Throwable")
        }
        return resourceIDs
    }

    fun getApplicationName(context: Context): String {
        val info = context.applicationInfo
        val resId = info.labelRes
        return if (resId != 0) context.getString(resId) else info.nonLocalizedLabel.toString()
    }

    fun getActivityName(activity: Activity): String? {
        try {
            val pm = activity.packageManager
            return pm.getActivityInfo(activity.componentName, 0).loadLabel(pm).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    fun getResId(context: Context, name: String, defType: String, log: Boolean = true): Int {
        val id = context.resources.getIdentifier(name, defType, context.packageName)
        if (log && id == 0) {
            Logger.t(this::class.simpleName).wtf("R.$defType.$name 不存在")
        }
        return id
    }
}
