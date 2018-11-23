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

package genos.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import genos.R
import genos.ui.activity.base.NavigationActivity
import kotlinx.android.synthetic.main.activity_drawer.*

abstract class DrawerActivity(var index: Int = 0) : NavigationActivity() {
    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var currentItem: MenuItem

    override fun onSetContentView(name: String) {
        setContentView(R.layout.activity_drawer)
        drawerLayout = drawer_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toggle = ActionBarDrawerToggle(
                this,
                drawerLayout,
                navigationBar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) // 不能onSetContentView中调用, navigationBar还未获取为null
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigation.setNavigationItemSelectedListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            currentItem.isChecked = false
            currentItem = it
            currentItem.isChecked = true
            onNavigationItemSelected(it)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        currentItem = navigation.menu[index]
        currentItem.isChecked = true
        onNavigationItemSelected(currentItem)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
