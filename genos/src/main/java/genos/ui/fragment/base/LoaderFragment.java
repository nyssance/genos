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

package genos.ui.fragment.base;

import android.os.Bundle;
import android.view.View;

import com.orhanobut.logger.Logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import genos.R;
import genos.ui.BaseViewModel;
import retrofit2.Call;

enum RefreshMode {
    didLoad, willAppear, didAppear, never
}

enum RefreshControlMode {
    always, never
}

abstract class LoaderFragment<D> extends BaseFragment {
    protected ViewModel mViewModel;
    protected Call<D> mCall;
    protected RefreshMode mRefreshMode = RefreshMode.didLoad;
    protected RefreshControlMode mRefreshControlMode = RefreshControlMode.always;
    protected boolean mIsLoading;

    protected SwipeRefreshLayout mSwipeRefresh;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 下拉刷新
        if (mRefreshControlMode == RefreshControlMode.always) {
            mSwipeRefresh = view.findViewById(R.id.swipe_refresh);
            if (mSwipeRefresh != null) {
                mSwipeRefresh.setColorSchemeResources(R.color.app_color);
                mSwipeRefresh.setOnRefreshListener(() -> {
                    mSwipeRefresh.setRefreshing(true);
                    onPerform(R.id.action_view_refresh);
                });
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = onCreateViewModel();
        onViewModelCreated();
        ((BaseViewModel<D>) mViewModel).data.observe(this, this::onDataChanged);
        if (mCall == null) { // 如果call为空, 刷新模式自动为never
            mRefreshMode = RefreshMode.never;
            mRefreshControlMode = RefreshControlMode.never;
        }
        if (mRefreshMode == RefreshMode.didLoad) {
            refresh();
        }
    }

    protected ViewModel onCreateViewModel() {
        return ViewModelProviders.of(this).get(BaseViewModel.class);
    }

    protected void onViewModelCreated() {
    }

    protected void onDataChanged(D data) {
        mIsLoading = false;
        if (mRefreshControlMode == RefreshControlMode.always) { // TODO: 不处理布局文件遗漏下拉刷新的情况
            if (mSwipeRefresh != null) {
                mSwipeRefresh.setRefreshing(false);
            }
        }
        if (data != null) {
            onLoadSuccess(data);
        } else {
            onLoadFailure(666, "loader data 为 null");
        }
    }

    protected abstract void onLoadSuccess(@NonNull D data);

    protected void onLoadFailure(int code, @NonNull String message) {
        Logger.t("base").w(message);
    }

    // MARK: - 💛 Action

    protected void refresh() {
        if (mCall != null) {
            mIsLoading = true;
            ((BaseViewModel<D>) mViewModel).loadData(mCall);
        }
    }
}
