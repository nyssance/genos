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

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Environment
import android.telephony.TelephonyManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
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

    // @IdRes
    @JvmStatic
    fun getResId(context: Context, name: String, defType: String): Int {
        val id = context.resources.getIdentifier(name, defType, context.packageName)
        if (id == 0) {
            Logger.t("helper").wtf("R.$defType.$name 不存在")
        }
        return id
    }

    // Color
    @JvmStatic
    @ColorInt
    fun getColor(context: Context, @ColorRes id: Int): Int {
        return ContextCompat.getColor(context, id)
    }

    // Drawable
    // SO: https://stackoverflow.com/questions/29041027/android-getresources-getdrawable-deprecated-api-22
    @JvmStatic
    fun getDrawable(context: Context, @DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(context, id)
        // return ResourcesCompat.getDrawable(context.getResources(), id,
        // theme)
    }

    // 其他
    @JvmStatic
    fun getColorFromIdentifier(context: Context, id: Int): Int {
        val id = getResId(context, context.resources.getResourceEntryName(id), "color")
        return if (id != 0) getColor(context, id) else 0
    }

    @JvmStatic
    fun getDrawableFromIdentifier(context: Context, id: Int): Drawable? {
        val id = getResId(context, context.resources.getResourceEntryName(id), "drawable")
        return if (id != 0) getDrawable(context, id) else null
    }

    @JvmStatic
    fun getApplicationName(context: Context): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(stringId)
    }

    @JvmStatic
    fun getActivityName(activity: FragmentActivity): CharSequence? {
        try {
            val pm = activity.packageManager
            return pm.getActivityInfo(activity.componentName, 0).loadLabel(pm)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }
}
