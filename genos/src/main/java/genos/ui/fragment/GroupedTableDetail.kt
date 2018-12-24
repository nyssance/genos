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

package genos.ui.fragment

import android.content.Context
import android.content.Intent
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nyssance.genos.R
import genos.Shortcuts.openLink
import genos.model.Item
import genos.startActivitySafely
import genos.ui.fragment.base.ListViewStyle
import genos.ui.fragment.base.RecyclerViewFragment
import genos.ui.viewholder.DefaultHolder
import genos.ui.viewholder.SubtitleHolder
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

abstract class GroupedTableDetail<D : Any, T : Item, VH : RecyclerView.ViewHolder> : RecyclerViewFragment<D, T, VH>() {
    @JvmField
    protected var items = ArrayList<T>()
    @JvmField
    protected var data: D? = null

    init {
        listViewStyle = ListViewStyle.Grouped
    }

    override fun onCreateLayoutManager(context: Context): RecyclerView.LayoutManager {
        // SO: https://stackoverflow.com/questions/41546983/add-margins-to-divider-in-recyclerview#47988965
        val attrs = context.obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
        val divider = attrs.getDrawable(0)
        val inset = resources.getDimensionPixelSize(R.dimen.start_keyline)
        val insetDivider = InsetDrawable(divider, 0, 0, 0, 0)
        attrs.recycle()
        val layoutManager = LinearLayoutManager(context)
        val decor = DividerItemDecoration(context, layoutManager.orientation)
        decor.setDrawable(insetDivider)
        listView.addItemDecoration(decor)
        return layoutManager
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

    override fun onDisplayItem(item: T, holder: VH, viewType: Int) {
        if (holder is DefaultHolder) {
            if (item.icon == null) {
                holder.icon.visibility = View.GONE
            } else {
                Glide.with(requireContext()).load(item.icon).into(holder.icon)
            }
            holder.title.text = item.title
            holder.accessory.visibility = if (item.enabled) View.VISIBLE else View.GONE
        }
        if (holder is SubtitleHolder) {
            data?.let {
                holder.subtitle.text = "${readProperty(it, item.name) ?: ""}"
            }
        }
    }

    override fun onOpenItem(item: T) {
        item.dest?.let {
            startActivitySafely(Intent(requireContext(), it))
            return
        }
        when (item.link.toUri().scheme) {
            "http" -> Toast.makeText(context, "为了安全不支持http, 请使用https链接。", Toast.LENGTH_LONG).show()
            "https" -> openLink(requireContext(), item.link, item.title)
        }
        // route(item.link)
    }

//    override fun refresh() {
//        onPrepare()  // 不做onPrepare()重新赋值call的话, HttpUtils里call不clone是会报错的
//        super.refresh()
//    }

    private fun readProperty(instance: Any, propertyName: String): Any? {
        instance::class.memberProperties.firstOrNull {
            it.visibility == KVisibility.PUBLIC && it.name == propertyName
        }?.apply {
            return call(instance)
        }
        return null
    }

    private fun setupUI() {
        adapter.removeAll()
        adapter.append(items)
        // 同步调用notifyDataSetChanged RecyclerView会报错
        val handler = Handler()
        val r = Runnable { adapter.notifyDataSetChanged() }
        handler.post(r)
    }
}
