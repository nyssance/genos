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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.core.app.TaskStackBuilder
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.orhanobut.logger.Logger
import genos.Helper
import genos.R
import genos.libs.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

abstract class BaseActivity : AppCompatActivity() {
    protected var collapsingToolbar: CollapsingToolbarLayout? = null
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
        val className = javaClass.simpleName
        Logger.t("base").i("$className : onCreate()")
        val name = className.replace("Activity", "")
                .replace("(.)(\\p{Upper})".toRegex(), "$1_$2")
                .toLowerCase(Locale.ENGLISH)
        onSetContentView(name)
        menuRes = Helper.getResId(this, name, "menu")
        // 可折叠顶栏
        collapsingToolbar = findViewById(R.id.collapsing_toolbar)
        // 顶部导航栏
        navigationBar = findViewById(R.id.navigation_bar)
        if (navigationBar != null) {
            setSupportActionBar(navigationBar)
        }
        // 底部工具栏
        toolbar = findViewById(R.id.toolbar)
        if (toolbar != null) {
            val resId = Helper.getResId(this, "${name}_toolbar", "menu")
            if (resId > 0) {
                toolbar?.inflateMenu(resId)
            }
        }
        // 默认显示 Up button
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            if (title == Helper.getApplicationName(this)) { // Activity android:label not set
                val resId = Helper.getResId(this, name, "string")
                if (resId > 0) {
                    actionBar.setTitle(resId)
                }
            }
        }
    }

    protected open fun onSetContentView(name: String) {
        val layoutName = "activity_$name"
        val layoutResID = Helper.getResId(this, layoutName, "layout")
        if (layoutResID > 0) { // NY: 如果子类重新setContentView并且和此处同名，同时包含fragment的话，会报错
            setContentView(layoutResID)
        } else {
            Logger.t("base").wtf("$layoutName.xml not exists!")
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
        // Wrong doc: https://developer.android.com/training/implementing-navigation/ancestral.html#up
        // SO: https://stackoverflow.com/questions/19999619/navutils-navigateupto-does-not-start-any-activity#31350642
        return when (item.itemId) {
            android.R.id.home -> {
                val upIntent = NavUtils.getParentActivityIntent(this)
                if (NavUtils.shouldUpRecreateTask(this, upIntent!!) || isTaskRoot) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities()
                } else { // NY: NavUtils依然会重载入父页面, 所以增加FLAG_ACTIVITY_CLEAR_TOP
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    NavUtils.navigateUpTo(this, upIntent)
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
            if (onKeyUpListener != null) {
                onKeyUpListener!!.onKeyUp(keyCode, event) || super.onKeyUp(keyCode, event)
            } else { // 如果为null, 执行系统默认
                super.onKeyUp(keyCode, event)
            }
        } else {
            false
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEventReceived(event: MessageEvent) {
        Logger.t("EventBus")
                .w("Activity: ${javaClass.simpleName} 收到了 Fragment: ${event.sender.javaClass.simpleName} 的消息: ${event.message}")
    }

    interface OnBackPressedListener {
        fun onBackPressed(): Boolean
    }

    interface OnKeyUpListener {
        fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean
    }
}
