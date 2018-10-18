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
    private val mData = ArrayList<T>()
    lateinit var keyProvider: ItemKeyProvider<T>
        private set
    lateinit var detailsLookup: ItemDetailsLookup<T>
        private set

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        // KeyProvider & DetailsLookup
        keyProvider = object : ItemKeyProvider<T>(SCOPE_CACHED) {
            override fun getKey(position: Int): T? {
                return getItem(position)
            }

            override fun getPosition(key: T): Int {
                return mData.indexOf(key)
            }
        }
        detailsLookup = object : ItemDetailsLookup<T>() {
            override fun getItemDetails(e: MotionEvent): ItemDetails<T>? {
                val view = recyclerView.findChildViewUnder(e.x, e.y)
                if (view != null) {
                    val holder = recyclerView.getChildViewHolder(view)
                    val position = holder.adapterPosition
                    return object : ItemDetailsLookup.ItemDetails<T>() {
                        override fun getPosition(): Int {
                            return position
                        }

                        override fun getSelectionKey(): T? {
                            return getItem(position)
                        }
                    }
                }
                return null
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun getItem(position: Int): T? {
        return mData[position]
    }

    fun append(items: Collection<T>) {
        mData.addAll(items)
    }

    fun removeAll() {
        mData.clear()
    }

    fun add(index: Int, item: T) {
        mData.add(index, item)
        notifyItemInserted(index) // TODO: 同步调用会不会出错
    }

    fun remove(index: Int) {
        mData.removeAt(index)
        notifyItemRemoved(index)
    }
}
