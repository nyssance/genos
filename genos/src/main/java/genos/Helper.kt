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
import android.graphics.drawable.Drawable
import android.os.Environment
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import com.orhanobut.logger.Logger

object Helper {
    // https://developer.android.com/guide/topics/data/data-storage.html#filesExternal
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
        try {
            for (i in 0 until count) {
                // pass 'null' because class is static
                resourceIDs[i] = resourceIDFields[i].getInt(null)
            }
        } catch (e: IllegalAccessException) {
            Logger.t("helper").e(e, "IllegalAccessException | IllegalArgumentException")
        } catch (e: IllegalArgumentException) {
            Logger.t("helper").e(e, "IllegalAccessException | IllegalArgumentException")
        }
        return resourceIDs
    }

    @JvmStatic
    fun isTablet(context: Context): Boolean {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE)
        if (manager is TelephonyManager) {
            return manager.phoneType == TelephonyManager.PHONE_TYPE_NONE
        }
        return false
    }

    @JvmStatic
    fun getApplicationName(context: Context): String {
        val info = context.applicationInfo
        val resId = info.labelRes
        return if (resId != 0) context.getString(resId) else info.nonLocalizedLabel.toString()
    }

    @JvmStatic
    fun getActivityName(activity: Activity): String? {
        try {
            val pm = activity.packageManager
            return pm.getActivityInfo(activity.componentName, 0).loadLabel(pm).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    @JvmStatic
    fun getResId(context: Context, name: String, defType: String, log: Boolean = true): Int {
        val id = context.resources.getIdentifier(name, defType, context.packageName)
        if (log && id == 0) {
            Logger.t("helper").wtf("R.$defType.$name 不存在")
        }
        return id
    }

    // 其他
    @JvmStatic
    fun getColorFromIdentifier(context: Context, id: Int): Int {
        val resId = getResId(context, context.resources.getResourceEntryName(id), "color")
        return if (resId != 0) ContextCompat.getColor(context, resId) else 0
    }

    @JvmStatic
    fun getDrawableFromIdentifier(context: Context, id: Int): Drawable? {
        val resId = getResId(context, context.resources.getResourceEntryName(id), "drawable")
        return if (resId != 0) context.getDrawable(resId) else null
    }
}
