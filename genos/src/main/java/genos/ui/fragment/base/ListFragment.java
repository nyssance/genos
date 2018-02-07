/*
 * Copyright 2018 NY (nyssance@icloud.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package genos.ui.fragment.base;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import static genos.BaseAppManager.LIST_START_PAGE;

public abstract class ListFragment<D, T, VH extends RecyclerView.ViewHolder> extends RecyclerViewFragment<D, T, VH> {
    protected int mPage = LIST_START_PAGE;
    protected boolean mCanLoadMore = true;

    @NonNull
    protected abstract List<T> TransformListFromData(@NonNull D data);

    @Override
    protected void onLoadSuccess(@NonNull D result) {
        if (mPage == LIST_START_PAGE) { // 如果无翻页或为第一页, 完全重载
            mAdapter.removeAll();
        }
        mAdapter.append(TransformListFromData(result));
        // 同步调用notifyDataSetChanged RecyclerView会报错
        Handler handler = new Handler();
        final Runnable r = mAdapter::notifyDataSetChanged;
        handler.post(r);
    }

    @Override
    protected void onLoadFailure(int statusCode, @NonNull String message) {
        super.onLoadFailure(statusCode, message);
        if (mPage > LIST_START_PAGE) {
            mPage--;
        }
    }

    @Override
    protected int onGetItemViewType(int position) {
        loadMore(mAdapter.getItemCount(), position);
        return super.onGetItemViewType(position);
    }

    @Override
    protected final void refresh() {
        mPage = LIST_START_PAGE;
        onPrepare();
        super.refresh();
    }

    protected void loadMore(int size, int position) {
        if ((mRefreshControlMode != RefreshControlMode.none && mSwipeRefresh != null && mSwipeRefresh.isRefreshing()) || mIsLoading) {
            return;
        }
        if (position == size - 1) {
            mPage++;
            onPrepare();
            super.refresh();
        }
    }
}
