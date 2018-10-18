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

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import genos.R
import genos.ui.BaseViewModel
import retrofit2.Call

enum class RefreshMode {
    didLoad, willAppear, didAppear, never
}

enum class RefreshControlMode {
    always, never
}

abstract class LoaderFragment<D> : BaseFragment() {
    protected lateinit var mViewModel: ViewModel
    protected var mCall: Call<D>? = null
    protected var mRefreshMode = RefreshMode.didLoad
    protected var mRefreshControlMode = RefreshControlMode.always
    protected var mIsLoading: Boolean = false

    protected var mSwipeRefresh: SwipeRefreshLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 下拉刷新
        if (mRefreshControlMode == RefreshControlMode.always) {
            mSwipeRefresh = view.findViewById(R.id.swipe_refresh)
            if (mSwipeRefresh != null) {
                mSwipeRefresh?.setColorSchemeResources(R.color.app_color)
                mSwipeRefresh?.setOnRefreshListener {
                    mSwipeRefresh?.isRefreshing = true
                    onPerform(R.id.action_view_refresh)
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = onCreateViewModel()
        onViewModelCreated()
        (mViewModel as BaseViewModel<D>).data.observe(this, Observer<D> { it -> this.onDataChanged(it) })
        if (mCall == null) { // 如果call为空, 刷新模式自动为never
            mRefreshMode = RefreshMode.never
            mRefreshControlMode = RefreshControlMode.never
        }
        if (mRefreshMode == RefreshMode.didLoad) {
            refresh()
        }
    }

    protected fun onCreateViewModel(): ViewModel {
        // SO: https://stackoverflow.com/questions/39679180/kotlin-call-java-method-with-classt-argument
        val ref = BaseViewModel<D>().javaClass // BaseViewModel<D>::class.java 不行
        return ViewModelProviders.of(this).get<BaseViewModel<D>>(ref)
    }

    protected fun onViewModelCreated() {}

    protected fun onDataChanged(data: D?) {
        mIsLoading = false
        if (mRefreshControlMode == RefreshControlMode.always) { // TODO: 不处理布局文件遗漏下拉刷新的情况
            if (mSwipeRefresh != null) {
                mSwipeRefresh?.isRefreshing = false
            }
        }
        if (data != null) {
            onLoadSuccess(data)
        } else {
            onLoadFailure(666, "loader data 为 null")
        }
    }

    protected abstract fun onLoadSuccess(data: D)

    protected open fun onLoadFailure(code: Int, message: String) {
        Logger.t("base").w(message)
    }

    // MARK: - 💛 Action

    protected open fun refresh() {
        if (mCall != null) {
            mIsLoading = true
            (mViewModel as BaseViewModel<D>).loadData(mCall!!)
        }
    }
}
