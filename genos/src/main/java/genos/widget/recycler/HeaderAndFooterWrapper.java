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

package genos.widget.recycler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import genos.ui.viewholder.BaseHolder;

public class HeaderAndFooterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArray<View> mHeaderViews = new SparseArray<>();
    private SparseArray<View> mFootViews = new SparseArray<>();

    private RecyclerView.Adapter mWrapped;

    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter) {
        mWrapped = adapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return new BaseHolder(mHeaderViews.get(viewType));
        } else if (mFootViews.get(viewType) != null) {
            return new BaseHolder(mFootViews.get(viewType));
        }
        return mWrapped.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isHeaderView(position)) {
            return;
        }
        if (isFooterView(position)) {
            return;
        }
        mWrapped.onBindViewHolder(holder, position - mHeaderViews.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterView(position)) {
            return mFootViews.keyAt(position - mHeaderViews.size() - mWrapped.getItemCount());
        }
        return mWrapped.getItemViewType(position - mHeaderViews.size());
    }

    @Override
    public int getItemCount() {
        return mHeaderViews.size() + mWrapped.getItemCount() + mFootViews.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mWrapped, recyclerView, (layoutManager, oldLookup, position) -> {
            int viewType = getItemViewType(position);
            if (mHeaderViews.get(viewType) != null) {
                return layoutManager.getSpanCount();
            } else if (mFootViews.get(viewType) != null) {
                return layoutManager.getSpanCount();
            }
            if (oldLookup != null) {
                return oldLookup.getSpanSize(position);
            }
            return 1;
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        mWrapped.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderView(position) || isFooterView(position)) {
            WrapperUtils.setFullSpan(holder);
        }
    }

    private boolean isHeaderView(int position) {
        return position < mHeaderViews.size();
    }

    private boolean isFooterView(int position) {
        return position >= mHeaderViews.size() + mWrapped.getItemCount();
    }

    public final void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public final void addFootView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public final int getHeaderViewsCount() {
        return mHeaderViews.size();
    }

    public final int getFooterViewsCount() {
        return mFootViews.size();
    }
}
