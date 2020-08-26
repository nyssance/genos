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
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nyssance.genos.R
import com.orhanobut.logger.Logger
import genos.ui.BaseViewModel
import retrofit2.Call

abstract class LoaderFragment<D : Any>(contentLayoutId: Int) : BaseFragment(contentLayoutId) {
    enum class RefreshMode { DidLoad, WillAppear, DidAppear, Never }
    enum class RefreshControlMode { Always, Never }

    protected lateinit var viewModel: ViewModel
    protected var call: Call<out D>? = null // out防止java中出现List<? extends T>
    protected var refreshMode = RefreshMode.DidLoad
    protected var refreshControlMode = RefreshControlMode.Always
    protected var isLoading = false

    protected var refreshControl: SwipeRefreshLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        refreshControl = view.findViewById(R.id.swipe_refresh)
        refreshControl?.isEnabled = false // 默认为开启，将其关闭
        if (call == null) { // 如果call为空, 刷新模式自动为never
            refreshMode = RefreshMode.Never
            refreshControlMode = RefreshControlMode.Never
        }
        // 下拉刷新
        if (refreshControlMode == RefreshControlMode.Always) {
            refreshControl?.apply {
                isEnabled = true
                setColorSchemeResources(R.color.colorPrimary)
                setOnRefreshListener {
                    isRefreshing = true
                    onPerform(R.id.action_view_refresh)
                }
            }
        }
        // 以前的 onActivityCreated 部分
        viewModel = onCreateViewModel()
        onViewModelCreated()
        (viewModel as BaseViewModel<D>).data.observe(viewLifecycleOwner, {
            isLoading = false
            activity?.runOnUiThread {
                onDataChanged(it)
            }
        })
        if (refreshMode == RefreshMode.DidLoad) {
            refresh()
        }
    }

    // SO https://stackoverflow.com/questions/39679180/kotlin-call-java-method-with-classt-argument
    // ViewModelProviders.of(this).get<BaseViewModel<D>>(BaseViewModel<D>().javaClass)
    // BaseViewModel<D>::class.java 不行
    protected open fun onCreateViewModel() = ViewModelProviders.of(this).get(BaseViewModel::class.java)

    protected open fun onViewModelCreated() {}

    protected open fun onDataLoadSuccess(status: Int) {
        Logger.t(this::class.simpleName).d("onDataLoadSuccess status $status")
    }

    protected open fun onDataLoadFailure(status: Int, message: String) {
        Logger.t(this::class.simpleName).w(message)
    }

    protected open fun onDataChanged(data: D) {
        if (refreshControlMode == RefreshControlMode.Always) {
            refreshControl?.isRefreshing = false
        }
        onDisplay(data)
    }

    protected abstract fun onDisplay(data: D)

    // 💛 Action

    protected open fun refresh() {
        if (call != null) {
            isLoading = true
            (viewModel as? BaseViewModel<D>)?.let {
                val c = call as Call<D>
                it.loadData(c, this::onDataLoadSuccess, this::onDataLoadFailure)
            }
        }
    }
}
