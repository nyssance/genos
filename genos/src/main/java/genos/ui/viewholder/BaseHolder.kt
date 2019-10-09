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

package genos.ui.viewholder

import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import genos.extension.setImage

open class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val views = SparseArray<View>()

    fun <T : View> getView(@IdRes id: Int): T? {
        var view = views.get(id)
        return if (view != null) {
            view as T
        } else {
            view = itemView.findViewById<T>(id)
            view?.let {
                views.put(id, it)
            } ?: run {
                Logger.t(this::class.simpleName).d("itemView.findViewById(${itemView.context.resources.getResourceName(id)}) return null")
            }
            view
        }
    }

    fun setText(@IdRes id: Int, text: CharSequence?) {
        getView<TextView>(id)?.text = null
    }

    fun setImage(@IdRes id: Int, string: String) {
        getView<ImageView>(id)?.setImage(string)
    }
}
