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
import com.orhanobut.logger.Logger
import genos.R
import genos.ui.BaseViewModel
import retrofit2.Call

enum class RefreshMode {
    DidLoad, WillAppear, DidAppear, Never
}

enum class RefreshControlMode {
    Always, Never
}

abstract class LoaderFragment<D> : BaseFragment() {
    protected lateinit var viewModel: ViewModel
    protected var call: Call<D>? = null
    protected var refreshMode = RefreshMode.DidLoad
    protected var refreshControlMode = RefreshControlMode.Always
    protected var isLoading = false

    protected var refreshControl: SwipeRefreshLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 下拉刷新
        if (refreshControlMode == RefreshControlMode.Always) {
            refreshControl = view.findViewById(R.id.swipe_refresh)
            refreshControl?.setColorSchemeResources(R.color.app_color)
            refreshControl?.setOnRefreshListener {
                refreshControl?.isRefreshing = true
                onPerform(R.id.action_view_refresh)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = onCreateViewModel()
        onViewModelCreated()
        (viewModel as BaseViewModel<D>).data.observe(this, Observer<D> { result -> this.onDataChanged(result) })
        if (call == null) { // 如果call为空, 刷新模式自动为never
            refreshMode = RefreshMode.Never
            refreshControlMode = RefreshControlMode.Never
        }
        if (refreshMode == RefreshMode.DidLoad) {
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
        isLoading = false
        if (refreshControlMode == RefreshControlMode.Always) { // TODO: 不处理布局文件遗漏下拉刷新的情况
            refreshControl?.isRefreshing = false
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
        if (call != null) {
            isLoading = true
            (viewModel as BaseViewModel<D>).loadData(call!!)
        }
    }
}
