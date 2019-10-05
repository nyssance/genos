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

package genos.ui.fragment.base

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import genos.BaseAppManager.Companion.LIST_START_PAGE

abstract class ListFragment<D : Any, T : Any, VH : RecyclerView.ViewHolder>(val spanCount: Int) : RecyclerViewFragment<D, T, VH>() {
    @JvmField
    protected var page = LIST_START_PAGE

    protected abstract fun transformListFromData(data: D): ArrayList<T>

    protected open fun hasNext(): Boolean {
        return true
    }

    protected open fun hasPrevious(): Boolean {
        return page > LIST_START_PAGE
    }

    override fun onDataLoadFailure(code: Int, message: String) {
        super.onDataLoadFailure(code, message)
        if (page > LIST_START_PAGE) {
            page--
        }
    }

    override fun onDisplay(data: D) {
        with(adapter) {
            if (!hasPrevious()) { // 如果无上一页, 完全重载
                removeAll()
            }
            addAll(transformListFromData(data))
        }
    }

    override fun onScrollChange(v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        //SO https://stackoverflow.com/questions/39894792/recyclerview-scrolllistener-inside-nestedscrollview#41262612
        if (scrollY == listView.measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
            loadMore()
        }
    }

    override fun refresh() {
        page = LIST_START_PAGE
        onCreate()
        super.refresh()
    }

    protected fun loadMore() {
        if (!hasNext() || isLoading) {
            return
        }
        isLoading = true
        page++
        onCreate()
        super.refresh()
    }
}
