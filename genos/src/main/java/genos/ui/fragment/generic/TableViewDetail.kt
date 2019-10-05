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

package genos.ui.fragment.generic

import android.content.Intent
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nyssance.genos.R
import genos.Utils.openLink
import genos.extension.navigateTo
import genos.extension.setImage
import genos.model.Item
import genos.ui.fragment.base.ListViewStyle
import genos.ui.fragment.base.RecyclerViewFragment
import genos.ui.viewholder.DefaultHolder
import genos.ui.viewholder.SubtitleHolder
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

abstract class TableViewDetail<D : Any, T : Item, VH : RecyclerView.ViewHolder> : RecyclerViewFragment<D, T, VH>() {
    @JvmField
    protected var items = ArrayList<T>()
    @JvmField
    protected var data: D? = null

    init {
        listViewStyle = ListViewStyle.Grouped
    }

    override fun onUpdateLayoutManager() {
        //SO https://stackoverflow.com/questions/41546983/add-margins-to-divider-in-recyclerview#47988965
        val context = requireContext()
        val attrs = context.obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
        val divider = attrs.getDrawable(0)
        val inset = resources.getDimensionPixelSize(R.dimen.start_keyline)
        val insetDivider = InsetDrawable(divider, 0, 0, 0, 0)
        attrs.recycle()
        val layoutManager = LinearLayoutManager(context)
        val decor = DividerItemDecoration(context, layoutManager.orientation)
        decor.setDrawable(insetDivider)
        listView.addItemDecoration(decor)
        listView.layoutManager = layoutManager
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUI()
    }

    override fun onDisplay(data: D) {
        this.data = data
        setupUI()
    }

    override fun onGetItemViewType(position: Int): Int {
        if (items[position].isSection) {
            adapter.sections.put(position, null)
        }
        return super.onGetItemViewType(position)
    }

    override fun onDisplayItem(item: T, view: VH, viewType: Int) {
        with(view) {
            if (this is DefaultHolder) {
                if (item.icon == null) {
                    icon.visibility = View.GONE
                } else {
                    icon.setImage(item.icon)
                }
                title.text = item.title
                accessory.visibility = if (item.enabled) View.VISIBLE else View.GONE
            }
            if (this is SubtitleHolder) {
                data?.let {
                    subtitle.text = "${readProperty(it, item.name) ?: ""}"
                }
            }
        }
    }

    override fun onOpenItem(item: T) {
        item.destination?.let {
            navigateTo(Intent(requireContext(), it))
            return
        }
        when (item.link.toUri().scheme) {
            "http" -> Toast.makeText(requireContext(), "为了安全不支持http, 请使用https链接。", Toast.LENGTH_LONG).show()
            "https" -> openLink(requireContext(), item.link, item.title)
        }
        // route(item.link)
    }

//    override fun refresh() {
//        onCreate()  // 不做onCreate()重新赋值call的话, HttpUtils里call不clone是会报错的
//        super.refresh()
//    }

    private fun readProperty(instance: Any, propertyName: String): Any? {
        instance::class.memberProperties.asSequence().firstOrNull {
            it.visibility == KVisibility.PUBLIC && it.name == propertyName
        }?.apply {
            return call(instance)
        }
        return null
    }

    private fun setupUI() {
        with(adapter) {
            removeAll()
            addAll(items)
        }
    }
}
