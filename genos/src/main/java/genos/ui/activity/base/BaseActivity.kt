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

package genos.ui.activity.base

import android.content.Intent
import android.os.Bundle
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
import genos.extension.pluralize
import java.util.Locale

abstract class BaseActivity(@LayoutRes val contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {
    var collapsingToolbar: CollapsingToolbarLayout? = null
    var navigationBar: Toolbar? = null
        protected set
    private var menuRes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val className = this::class.simpleName ?: "BaseActivity"
        Logger.i("$className :: onCreate()")
        val name = className.removeSuffix("Activity")
            .replace("(.)(\\p{Upper})".toRegex(), "$1_$2")
            .lowercase(Locale.ENGLISH)
        menuRes = Helper.getResId(this, name, "menu", false)
        // 可折叠顶栏
        collapsingToolbar = findViewById(R.id.collapsing_toolbar)
        // 顶部导航栏
        navigationBar = findViewById<Toolbar>(R.id.navigation_bar)?.apply(this::setSupportActionBar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true) // 默认显示 Up button
            if (title == Helper.getApplicationName(this)) { // Activity android:label not set
                val stringName = when {
                    name.endsWith("_list") -> name.removeSuffix("_list").pluralize()
                    name.endsWith("_detail") -> name.removeSuffix("_detail")
                    else -> name
                }
                val resId = Helper.getResId(this, stringName, "string")
                if (resId > 0) it.setTitle(resId) else it.title = title
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu) = when (menuRes) {
        in 1..Int.MAX_VALUE -> {
            menuInflater.inflate(menuRes, menu)
            true
        }

        else -> super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // SO https://stackoverflow.com/questions/19999619/navutils-navigateupto-does-not-start-any-activity#31350642
        android.R.id.home -> {
            NavUtils.getParentActivityIntent(this)?.let {
                if (NavUtils.shouldUpRecreateTask(this, it) || isTaskRoot) {
                    TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(it)
                        .startActivities()
                } else { // NY - NavUtils依然会重载入父页面, 所以增加FLAG_ACTIVITY_CLEAR_TOP
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
