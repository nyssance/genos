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

package genos.ui

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    private val data = ArrayList<T>()
    lateinit var keyProvider: ItemKeyProvider<Long>
        private set
    lateinit var detailsLookup: ItemDetailsLookup<Long>
        private set

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        // KeyProvider & DetailsLookup
        // https://medium.com/@Dalvin/android-recycler-view-with-multiple-item-selections-b2af90eb5825
        keyProvider = object : ItemKeyProvider<Long>(SCOPE_MAPPED) {
            override fun getKey(position: Int): Long? {
                return position.toLong()
            }

            override fun getPosition(key: Long): Int {
                return key.toInt()
            }
        }
        detailsLookup = object : ItemDetailsLookup<Long>() {
            override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
                val view = recyclerView.findChildViewUnder(e.x, e.y)
                if (view != null) {
                    val holder = recyclerView.getChildViewHolder(view)
                    val position = holder.adapterPosition
                    return object : ItemDetails<Long>() {
                        override fun getPosition(): Int {
                            return position
                        }

                        override fun getSelectionKey(): Long? {
                            return position.toLong()
                        }
                    }
                }
                return null
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getItem(position: Int): T {
        return data[position]
    }

    fun append(items: Collection<T>) {
        data.addAll(items)
    }

    fun removeAll() {
        data.clear()
    }

    fun add(index: Int, item: T) {
        data.add(index, item)
        notifyItemInserted(index) // TODO: 同步调用会不会出错
    }

    fun remove(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
    }
}
