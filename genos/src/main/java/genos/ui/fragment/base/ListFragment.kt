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

import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import genos.BaseAppManager.Companion.LIST_START_PAGE

//import genos.BaseAppManager.LIST_START_PAGE

abstract class ListFragment<D, T, VH : RecyclerView.ViewHolder> : RecyclerViewFragment<D, T, VH>() {
    protected var mPage = LIST_START_PAGE

    protected abstract fun transformListFromData(data: D): List<T>

    protected fun hasNext(): Boolean {
        return true
    }

    protected fun hasPrevious(): Boolean {
        return mPage > LIST_START_PAGE
    }

    override fun onLoadSuccess(data: D) {
        if (!hasPrevious()) { // 如果无上一页, 完全重载
            mAdapter.removeAll()
        }
        mAdapter.append(transformListFromData(data))
        // 同步调用notifyDataSetChanged RecyclerView会报错
        val handler = Handler()
        val r = Runnable { mAdapter.notifyDataSetChanged() }
        handler.post(r)
    }

    override fun onLoadFailure(code: Int, message: String) {
        super.onLoadFailure(code, message)
        if (mPage > LIST_START_PAGE) {
            mPage--
        }
    }

    override fun onGetItemViewType(position: Int): Int {
        loadMore(mAdapter.itemCount, position)
        return super.onGetItemViewType(position)
    }

    override fun refresh() {
        mPage = LIST_START_PAGE
        onPrepare()
        super.refresh()
    }

    protected fun loadMore(size: Int, position: Int) {
        if (!hasNext() || mIsLoading) {
            return
        }
        if (position == size - 1) {
            mIsLoading = true
            mPage++
            onPrepare()
            super.refresh()
        }
    }
}
