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

package genos.model

import android.content.Context
import androidx.annotation.StringRes

abstract class BaseItem constructor(
    context: Context?,
    @StringRes val id: Int,
    name: String? = null,
    val icon: Any? = null,
    title: String? = null,
    val subtitle: String? = null,
    var enabled: Boolean = false
) {
    val name: String = name ?: context?.resources?.getResourceEntryName(id) ?: ""
    val title = title ?: context?.getString(id) ?: ""
}
