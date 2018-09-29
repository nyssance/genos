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

package genos;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import android.telephony.TelephonyManager;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;

public class Helper {

    private static int[] getAllResourceIDs(Class<?> dataClass) { // 传入class为.R
        Field[] IDFields = dataClass.getFields();
        int[] IDs = new int[IDFields.length];
        int count = IDFields.length;
        try {
            for (int i = 0; i < count; i++) {
                // pass 'null' because class is static
                IDs[i] = IDFields[i].getInt(null);
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            Logger.t("helper").e(e, "IllegalAccessException | IllegalArgumentException");
        }
        return IDs;
    }

    // https://developer.android.com/guide/topics/data/data-storage.html#filesExternal
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static boolean isTablet(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getPhoneType() == TelephonyManager.PHONE_TYPE_NONE;
    }

    // @IdRes
    public static int getResId(Context context, String name, String defType) {
        int resId = context.getResources().getIdentifier(name, defType, context.getPackageName());
        if (resId == 0) {
            Logger.t("helper").wtf("R." + defType + "." + name + " 不存在");
        }
        return resId;
    }

    // Color
    public static int getColor(Context context, @ColorRes int id) {
        return ContextCompat.getColor(context, id);
    }

    // Drawable
    // SO: https://stackoverflow.com/questions/29041027/android-getresources-getdrawable-deprecated-api-22
    public static Drawable getDrawable(Context context, @DrawableRes int id) {
        return ContextCompat.getDrawable(context, id);
        // return ResourcesCompat.getDrawable(context.getResources(), id,
        // theme);
    }

    // 其他
    public static int getColorFromIdentifier(Context context, int id) {
        final int resId = getResId(context, context.getResources().getResourceEntryName(id), "color");
        return resId != 0 ? getColor(context, resId) : 0;
    }

    @Nullable
    public static Drawable getDrawableFromIdentifier(Context context, int id) {
        final int resId = getResId(context, context.getResources().getResourceEntryName(id), "drawable");
        return resId != 0 ? getDrawable(context, resId) : null;
    }

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public static CharSequence getActivityName(FragmentActivity activity) {
        try {
            PackageManager pm = activity.getPackageManager();
            return pm.getActivityInfo(activity.getComponentName(), 0).loadLabel(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
