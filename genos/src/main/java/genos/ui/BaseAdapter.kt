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

package genos.ui

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

interface Idable {
    val id: Long
}

class DiffCallback<T : Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) =
        if (this.javaClass is Idable) (oldItem as Idable).id == (newItem as Idable).id
        else oldItem.toString() == newItem.toString()

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
}

//abstract class BaseAdapter<T : Any, VH : RecyclerView.ViewHolder>(
//    diffCallback: DiffUtil.ItemCallback<T> = DiffCallback()
//) : ListAdapter<T, VH>(diffCallback) {
abstract class BaseAdapter<T : Any, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    private val currentList = ArrayList<T>()
    lateinit var keyProvider: ItemKeyProvider<Long>
        private set
    lateinit var detailsLookup: ItemDetailsLookup<Long>
        private set
    val sections = LinkedHashMap<Int, String?>()

    init {
        sections[0] = null
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        // KeyProvider & DetailsLookup
        // https://medium.com/@Dalvin/android-recycler-view-with-multiple-item-selections-b2af90eb5825
        keyProvider = object : ItemKeyProvider<Long>(SCOPE_MAPPED) {
            override fun getKey(position: Int) = position.toLong()

            override fun getPosition(key: Long) = key.toInt()
        }
        detailsLookup = object : ItemDetailsLookup<Long>() {
            override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
                recyclerView.findChildViewUnder(e.x, e.y)?.let {
                    val position = recyclerView.getChildViewHolder(it).bindingAdapterPosition
                    return object : ItemDetails<Long>() {
                        override fun getPosition() = position

                        override fun getSelectionKey() = position.toLong()
                    }
                }
                return null
            }
        }
    }

    override fun getItemCount() = currentList.size

//    public override fun getItem(position: Int): T = super.getItem(position)

    fun getItem(position: Int) = currentList[position]

    fun submitList(items: List<T>) {
        currentList.clear()
        currentList.addAll(items)
        notifyDataSetChanged()
    }

    fun addList(items: List<T>) {
        val start = itemCount
        currentList.addAll(items)
        notifyItemRangeChanged(start, itemCount)
    }

    fun insert(index: Int, item: T) {
        currentList.add(index, item)
        notifyItemInserted(index)
    }

    fun remove(index: Int) {
        currentList.removeAt(index)
        notifyItemRemoved(index)
    }
}
