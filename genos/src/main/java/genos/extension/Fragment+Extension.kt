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

package genos.extension

import android.content.DialogInterface
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.orhanobut.logger.Logger
import genos.Global.APP_SCHEME
import genos.model.Item
import genos.ui.activity.WebActivity
import genos.ui.fragment.ActionSheet
import genos.ui.fragment.Dialog
import java.util.*
import java.util.regex.Pattern.CASE_INSENSITIVE
import java.util.regex.Pattern.compile

typealias Routes = Set<Pair<String, Intent>>

var routes: Routes = emptySet()

fun Fragment.navigateTo(link: String, title: String = "") {
    if (link.isBlank()) {
        return
    }
    val uri = (if (link.contains("://")) link else "$APP_SCHEME://$link").trim().toUri()
    when (uri.scheme) {
        APP_SCHEME -> {
            routes.firstOrNull {
                compile(it.component1(), CASE_INSENSITIVE).matcher(link.removePrefix("$APP_SCHEME://")).find()
            }?.let {
                navigateTo(it.component2())
            } ?: run {
                val host = uri.host.orEmpty()
                val name = when (host) {
                    "" -> "version" // 仿Chrome, chrome://跳转到chrome://version
                    "about", "credits", "discards", "help",
                    "settings", "system", "profile", "version" -> host
                    else -> "${host.singularize()}${if (uri.path.orEmpty().isBlank()) "List" else "Detail"}"
                }.capitalize(Locale.ENGLISH)
                try {
                    navigateTo(Intent(context, Class.forName("${context?.packageName}.ui.${name}Activity")).apply {
                        data = uri
                    })
                } catch (cause: Throwable) {
                    showAlert("ERR_UNKNOWN_URI", cause.localizedMessage)
                }
            }
        }
        "https" -> navigateTo(Intent(context, WebActivity::class.java).apply {
            data = uri
            putExtra("title", title)
        })
        else -> showAlert("ERR_UNKNOWN_URL_SCHEME", link)
    }
}

fun Fragment.navigateTo(intent: Intent, isNewTask: Boolean = false) { // 默认为不开启外部Activity
    if (isNewTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 该句决定是否在同一进程中
    }
    try {
        Logger.wtf(intent.data.toString())
        startActivity(intent)
    } catch (cause: Throwable) {
        Toast.makeText(context, cause.localizedMessage, Toast.LENGTH_LONG).show()
    }
}

fun Fragment.showActionSheet(title: String, items: List<Item>, action: (Item) -> Unit) {
    ActionSheet.instance(title, items, action).show(parentFragmentManager, null)
}

fun Fragment.showAlert(title: String, message: String? = null, action: ((DialogInterface, Int) -> Unit)? = null) {
    Dialog(title, message, action).show(parentFragmentManager, null)
}
