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

package genos.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.HeaderViewListAdapter
import android.widget.ListAdapter
import android.widget.ListView
import com.nyssance.genos.R

class SectionListView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = android.R.attr.listViewStyle
) : ListView(context, attrs, defStyle) {
    lateinit var pinnedHeader: View
        private set // 不能为null，必须设一个值

    // 该方法目前没什么地方用到，暂存
    // 用getHeaderViewsCount() > 0 || getFooterViewsCount() >
    // 0判断不准，全为0时也可能是HeaderViewListAdapter
    val listAdapter: ListAdapter
        get() {
            val adapter = adapter
            return if (adapter is HeaderViewListAdapter) {
                adapter.wrappedAdapter
            } else adapter
        }

    init {
        pinnedHeader = LayoutInflater.from(context).inflate(R.layout.section_header, this, false)
    }

    fun setPinnedHeader(inflater: LayoutInflater, resource: Int) {
        pinnedHeader = inflater.inflate(resource, this, false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChild(pinnedHeader, widthMeasureSpec, heightMeasureSpec) // 测量子布局的尺寸
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        pinnedHeader.layout(0, 0, pinnedHeader.measuredWidth, pinnedHeader.measuredHeight) // 布局
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawChild(canvas, pinnedHeader, drawingTime)
    }
}// 这个构造函数在xml布局时调用，不能省略
