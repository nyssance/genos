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
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import genos.models.Item
import genos.ui.fragment.base.RecyclerViewFragment

abstract class GroupedTableDetail<D : Any, VH : RecyclerView.ViewHolder> : RecyclerViewFragment<D, Item, VH>() {
    @JvmField
    protected var items = ArrayList<Item>()

    override fun onCreateLayoutManager(context: Context): RecyclerView.LayoutManager {
        val layoutManager = LinearLayoutManager(context)
        listView.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
        return layoutManager
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter.removeAll()
        adapter.append(items)
        // 同步调用notifyDataSetChanged RecyclerView会报错
        val handler = Handler()
        val r = Runnable { adapter.notifyDataSetChanged() }
        handler.post(r)
    }

    override fun onDisplay(data: D) {
        adapter.removeAll()
        adapter.append(items)
        // 同步调用notifyDataSetChanged RecyclerView会报错
        val handler = Handler()
        val r = Runnable { adapter.notifyDataSetChanged() }
        handler.post(r)
    }
}
