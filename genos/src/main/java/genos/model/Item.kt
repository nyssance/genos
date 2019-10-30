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

package genos.model

import android.app.Activity
import android.content.Context
import androidx.annotation.StringRes
import androidx.collection.SimpleArrayMap
import genos.Global.APP_SCHEME
import java.util.*

open class Item constructor(
        context: Context,
        @StringRes id: Int,
        name: String? = null,
        icon: Any? = null,
        title: String? = null,
        subtitle: String? = null,
        val destination: Class<out Activity>? = null,
        link: String = "",
        enabled: Boolean = false,
        val choices: SimpleArrayMap<String, String> = SimpleArrayMap(),
        val isSection: Boolean = false
) : BaseItem(context, id, name, icon, title, subtitle, enabled) {
    var link = ""
        set(value) {
            field = value
            enabled = (destination != null || link.isNotBlank())
        }

    init {
        if (link.isBlank()) {
            destination?.let {
                var str = (it::class.simpleName ?: "unknown").toLowerCase(Locale.ROOT)
                str = when {
                    str.endsWith("list") -> str.replace("list", "s")
                    str.endsWith("detail") -> str.removeSuffix("detail")
                    str.endsWith("create") -> str.replace("create", "s/create")
                    else -> str
                }
                this.link = "$APP_SCHEME://$str"
            }
        } else {
            this.link = link.trim()
        }
        this.enabled = enabled || this.link.isNotBlank()
    }
}
