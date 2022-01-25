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

package genos.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.nyssance.genos.R
import genos.Global.LIST_START_PAGE

abstract class ListFragment<D : Any, T : Any, VH : RecyclerView.ViewHolder>(
    val spanCount: Int
) : RecyclerViewFragment<D, T, VH>() {
    protected var page = LIST_START_PAGE

    protected abstract fun transformListFromData(data: D): List<T>

    protected open fun hasNext() = true

    protected open fun hasPrevious() = page > LIST_START_PAGE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        super.onCreateView(inflater, container, savedInstanceState).apply {
            // SO https://stackoverflow.com/questions/39894792/recyclerview-scrolllistener-inside-nestedscrollview#41262612
            findViewById<NestedScrollView>(R.id.nested)?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                if (scrollY == listView.measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                    loadMore()
                }
            })
        }

    override fun onDataLoadFailure(status: Int, message: String) {
        super.onDataLoadFailure(status, message)
        if (page > LIST_START_PAGE) {
            page--
        }
    }

    override fun onDisplay(data: D) {
        with(adapter) {
            val items = transformListFromData(data)
            if (hasPrevious()) addList(items) else submitList(items) // 如果无上一页, 完全重载
        }
    }

    override fun refresh() {
        page = LIST_START_PAGE
        onCreate(requireActivity().intent)
        super.refresh()
    }

    protected fun loadMore() {
        if (!hasNext() || isLoading) {
            return
        }
        isLoading = true
        page++
        onCreate(requireActivity().intent)
        super.refresh()
    }
}
