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

package genos.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nyssance.genos.R
import genos.extension.setImage
import genos.model.Item
import genos.ui.BaseAdapter
import genos.ui.viewholder.Holder

// https://material.io/develop/android/components/bottom-sheet-dialog-fragment/

class ActionSheet(
    private val title: String,
    private val items: List<Item>,
    private val action: (Item) -> Unit
) : BottomSheetDialogFragment() {
    companion object {
        fun instance(title: String, items: List<Item>, action: (Item) -> Unit) = ActionSheet(title, items, action)
    }
//    private lateinit var binding: FragmentActionSheetBinding

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        binding = FragmentActionSheetBinding.inflate(inflater, container, false)
//        return binding.root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = object : BaseAdapter<Item, Holder>() {
            override fun getItemViewType(position: Int) = R.layout.list_item_default

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))

            override fun onBindViewHolder(holder: Holder, position: Int) {
                val item = getItem(position)
                with(holder) {
                    item.icon?.let {
                        icon?.setImage(it)
                    } ?: run {
                        icon?.visibility = View.GONE
                    }
                    title?.text = item.name
                    accessory?.visibility = View.GONE
                }
            }
        }
        val list = view.findViewById<RecyclerView>(android.R.id.list)
        list.adapter = adapter
        SelectionTracker.Builder(
            "selection-id",
            list, adapter.keyProvider, adapter.detailsLookup,
            StorageStrategy.createLongStorage()
        )
            .withOnItemActivatedListener { item, _ ->
                action(adapter.getItem(item.position))
                true
            }
            .build()
        adapter.addList(items)
    }
}
