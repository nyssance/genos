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

abstract class BaseItem {
    var id = 0
    var name: String
    var icon: Drawable? = null
    var title: String
    var subtitle: String? = null
    var enabled = false

    constructor(context: Context, @StringRes id: Int, name: String? = null, icon: Drawable? = null, title: String? = null, subtitle: String? = null, enabled: Boolean = false) {
        this.id = id
        this.name = name ?: context.resources.getResourceEntryName(id)
        this.icon = icon
        this.title = title ?: context.getString(id)
        this.subtitle = subtitle
        this.enabled = enabled
    }
}
