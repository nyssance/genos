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

package genos.ui.fragment.generic

import android.content.Intent
import androidx.annotation.LayoutRes
import com.nyssance.genos.R
import genos.ui.fragment.base.ObjectFragment

abstract class Detail<D : Any>(@LayoutRes contentLayoutId: Int = R.layout.fragment_detail) : ObjectFragment<D>(contentLayoutId) {
    final override fun onCreate(intent: Intent) {
        onCreate(intent, intent.data?.path?.removePrefix("/").orEmpty())
    }

    abstract fun onCreate(intent: Intent, id: String)
}
