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

import android.graphics.drawable.Drawable
import androidx.fragment.app.FragmentActivity
import genos.BaseAppManager.Companion.APP_SCHEME

class Item : BaseItem {
    var choices = HashMap<String, String>()
    var dest: FragmentActivity? = null
    var link: String = ""

    constructor(name: String, icon: Drawable? = null, title: String? = null, subtitle: String? = null,
                dest: FragmentActivity? = null, link: String = "",
                enabled: Boolean = false, choices: HashMap<String, String> = HashMap()) : super(name, icon, title, subtitle, enabled) {
        this.choices = choices
        this.dest = dest
        if (link.isBlank()) {
            dest?.let {
                var str = dest.javaClass.simpleName.toLowerCase()
                if (str.endsWith("list")) {
                    str = str.replace("list", "_list")
                } else if (str.endsWith("detail")) {
                    str = str.replace("detail", "_detail")
                } else if (str.endsWith("create")) {
                    str = str.replace("create", "_create")
                }
                this.link = "$APP_SCHEME://$str"
            }
        } else {
            this.link = link
        }
        this.enabled = enabled || this.link.isNotBlank()
    }
}
