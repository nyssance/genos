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

package genos.models

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import genos.BaseAppManager.Companion.APP_SCHEME

class Item : BaseItem {
    var choices = HashMap<String, String>()
    var dest: FragmentActivity? = null
    var link = ""

    constructor(context: Context, @StringRes id: Int, name: String? = null,
                icon: Drawable? = null, title: String? = null, subtitle: String? = null,
                dest: FragmentActivity? = null, link: String = "", enabled: Boolean = false,
                choices: HashMap<String, String> = HashMap()) : super(context, id, name, icon, title, subtitle, enabled) {
        this.choices = choices
        this.dest = dest
        if (link.isBlank()) {
            dest?.let {
                var str = dest.javaClass.simpleName.toLowerCase()
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
