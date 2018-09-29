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

package genos.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import genos.R;

public class SectionListView extends ListView {
    private View mPinnedHeader; // 不能为null，必须设一个值

    public SectionListView(Context context) {
        this(context, null);
    }

    public SectionListView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle); // 这个构造函数在xml布局时调用，不能省略
    }

    public SectionListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPinnedHeader = LayoutInflater.from(context).inflate(R.layout.section_header, this, false);
    }

    public View getPinnedHeader() {
        return mPinnedHeader;
    }

    public void setPinnedHeader(LayoutInflater inflater, int resource) {
        mPinnedHeader = inflater.inflate(resource, this, false);
    }

    public ListAdapter getListAdapter() { // 该方法目前没什么地方用到，暂存
        // 用getHeaderViewsCount() > 0 || getFooterViewsCount() >
        // 0判断不准，全为0时也可能是HeaderViewListAdapter
        final ListAdapter adapter = getAdapter();
        if (adapter instanceof HeaderViewListAdapter) {
            return ((HeaderViewListAdapter) adapter).getWrappedAdapter();
        }
        return adapter;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChild(mPinnedHeader, widthMeasureSpec, heightMeasureSpec); // 测量子布局的尺寸
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mPinnedHeader.layout(0, 0, mPinnedHeader.getMeasuredWidth(), mPinnedHeader.getMeasuredHeight()); // 布局
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawChild(canvas, mPinnedHeader, getDrawingTime());
    }
}
