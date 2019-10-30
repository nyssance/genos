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

package genos.extension

import android.content.DialogInterface
import android.content.Intent
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import genos.model.Item
import genos.ui.fragment.ActionSheet
import genos.ui.fragment.Dialog

// NY: ContextCompat, ViewCompat里有很多版本兼容方法, 不用自己写

@ColorInt
fun Fragment.getColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(requireContext(), id)
}

fun Fragment.navigate(intent: Intent) {
    startActivitySafely(intent)
}

fun Fragment.showActionSheet(title: String, items: List<Item>, action: (Item) -> Unit) {
    ActionSheet.instance(title, items, action).show(requireActivity().supportFragmentManager, null)
}

fun Fragment.showAlert(title: String, message: String? = null, action: ((DialogInterface, Int) -> Unit)? = null) {
    Dialog(title, message, action).show(requireActivity().supportFragmentManager, null)
}

fun Fragment.startActivitySafely(intent: Intent, isNewTask: Boolean = false) { // 默认为不开启外部Activity
    if (isNewTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 该句决定是否在同一进程中
    }
    try {
        startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
    }
}
