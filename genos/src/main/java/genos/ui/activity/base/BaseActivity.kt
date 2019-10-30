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

package genos.ui.activity.base

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.core.app.TaskStackBuilder
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.nyssance.genos.R
import com.orhanobut.logger.Logger
import genos.Helper
import genos.vendor.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

abstract class BaseActivity(@LayoutRes val contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {
    var collapsingToolbar: CollapsingToolbarLayout? = null
    var navigationBar: Toolbar? = null
        protected set
    var toolbar: Toolbar? = null
        protected set
    private var menuRes = 0
    private var onBackPressedListener: OnBackPressedListener? = null
    private var onKeyUpListener: OnKeyUpListener? = null

    fun setOnBackPressedListener(listener: OnBackPressedListener) {
        onBackPressedListener = listener
    }

    fun setOnKeyUpListener(listener: OnKeyUpListener) {
        onKeyUpListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val className = this::class.simpleName ?: "BaseActivity"
        Logger.t(className).i("$className :: onCreate()")
        val name = className.replace("Activity", "")
                .replace("(.)(\\p{Upper})".toRegex(), "$1_$2")
                .toLowerCase(Locale.ENGLISH)
        menuRes = Helper.getResId(this, name, "menu", false)
        // 可折叠顶栏
        collapsingToolbar = findViewById(R.id.collapsing_toolbar)
        // 顶部导航栏
        navigationBar = findViewById<Toolbar>(R.id.navigation_bar)?.apply(this::setSupportActionBar)
        // 底部工具栏
        toolbar = findViewById<Toolbar>(R.id.toolbar)?.apply {
            val id = Helper.getResId(this@BaseActivity, "${name}_toolbar", "menu")
            if (id > 0) {
                inflateMenu(id)
            }
        }
        // 默认显示 Up button
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            if (title == Helper.getApplicationName(this)) { // Activity android:label not set
                val id = Helper.getResId(this, name, "string")
                if (id > 0) {
                    it.setTitle(id)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (menuRes > 0) {
            menuInflater.inflate(menuRes, menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // SO https://stackoverflow.com/questions/19999619/navutils-navigateupto-does-not-start-any-activity#31350642
        return when (item.itemId) {
            android.R.id.home -> {
                NavUtils.getParentActivityIntent(this)?.let {
                    if (NavUtils.shouldUpRecreateTask(this, it) || isTaskRoot) {
                        TaskStackBuilder.create(this)
                                .addNextIntentWithParentStack(it)
                                .startActivities()
                    } else { // NY: NavUtils依然会重载入父页面, 所以增加FLAG_ACTIVITY_CLEAR_TOP
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        NavUtils.navigateUpTo(this, it)
                    }
                } ?: run {
                    onBackPressed() // 未手动添加 android:parentActivityName 的时候默认等同返回键
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (onBackPressedListener?.onBackPressed() != true) { // null or false
            super.onBackPressed()
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return if (event.isTracking && !event.isCanceled) {
            onKeyUpListener?.let {
                it.onKeyUp(keyCode, event) || super.onKeyUp(keyCode, event)
            } ?: run {
                super.onKeyUp(keyCode, event) // 如果为null, 执行系统默认
            }
        } else false
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEventReceived(event: MessageEvent) {
        Logger.t("EventBus")
                .w("【Activity】${this::class.simpleName} 收到了【Fragment】${event.sender::class.simpleName} 的消息: ${event.message}")
    }

    interface OnBackPressedListener {
        fun onBackPressed(): Boolean
    }

    interface OnKeyUpListener {
        fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean
    }
}
