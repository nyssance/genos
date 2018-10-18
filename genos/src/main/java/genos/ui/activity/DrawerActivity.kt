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

import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import genos.R
import genos.ui.activity.base.NavigationActivity

abstract class DrawerActivity : NavigationActivity() {
    protected lateinit var mDrawer: DrawerLayout
    protected var mNavigation: NavigationView? = null

    override fun onSetContentView(name: String) {
        setContentView(R.layout.activity_drawer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDrawer = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, mDrawer, navigationBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mDrawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigation = findViewById<NavigationView>(R.id.navigation)
        navigation.setNavigationItemSelectedListener { item ->
            mDrawer.closeDrawer(GravityCompat.START)
            onNavigationItemSelected(item.itemId)
        }
    }

    override fun onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
