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

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.nyssance.genos.R
import com.orhanobut.logger.Logger
import genos.ui.activity.base.BaseActivity

abstract class BaseFragment : Fragment(), BaseActivity.OnBackPressedListener, BaseActivity.OnKeyUpListener {
    // 💖 Lifecycle
    // https://developer.android.com/guide/components/fragments.html#Lifecycle

    /**
     * onAttach() : 绑定Activity的callback
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onPrepare()
    }

    /**
     * onPrepare() : 初始化, call, tileId, setHasOptionsMenu
     */
    protected abstract fun onPrepare()

    /**
     * onCreateView() : 布局, Fragment会被混淆, 所以都需要手动设置
     */

    /**
     * onViewCreated() : ensureList()，各种findViewById获取View
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<FloatingActionButton>(R.id.fab)?.setOnClickListener {
            Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * onActivityCreated() : 设置Adapter，List选中样式等在这里添加，加载数据前的最终初始化
     */

    /**
     * onPause() : 需要持久化纪录状态的写在这里, 因为用户可能不返回了
     */

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return onPerform(item.itemId)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        Logger.t("base").i("onKeyUp keyCode: $keyCode")
        return when (keyCode) {
            KeyEvent.KEYCODE_SEARCH -> true // 处理一些设备的专用search按钮
            KeyEvent.KEYCODE_F1 -> onPerform(R.id.action_help)
            KeyEvent.KEYCODE_F5 -> onPerform(R.id.action_view_refresh)
            KeyEvent.KEYCODE_ENTER -> true
            KeyEvent.KEYCODE_ESCAPE -> true
            else -> false
        }
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    protected abstract fun onPerform(action: Int): Boolean

    /**
     * 工具方法
     */
    @JvmOverloads
    fun startActivitySafely(intent: Intent, isNewTask: Boolean = false) { // 默认为不开启外部Activity
        if (isNewTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 该句决定是否在同一进程中
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    fun setTitle(title: CharSequence) {
        val activity = requireActivity()
        if (activity is AppCompatActivity) {
            activity.supportActionBar?.let {
                it.title = title
            } ?: run {
                Logger.t("base").w("No action bar!")
            }
        }
    }
}
