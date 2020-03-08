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
import androidx.fragment.app.Fragment
import com.nyssance.genos.R
import genos.ui.activity.base.NavigationActivity
import kotlinx.android.synthetic.main.activity_drawer.*

abstract class DrawerActivity(
        fragments: Map<Int, Fragment>,
        index: Int = 0
) : NavigationActivity(fragments, index, R.layout.activity_drawer) {
    protected lateinit var currentItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toggle = ActionBarDrawerToggle(
                this,
                drawer_layout,
                navigationBar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        navigation.setNavigationItemSelectedListener {
            drawer_layout.closeDrawer(GravityCompat.START)
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
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) drawer_layout.closeDrawer(GravityCompat.START) else super.onBackPressed()
    }
}
