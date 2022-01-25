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

package genos.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.nyssance.genos.R
import com.nyssance.genos.databinding.ActivityDrawerBinding
import genos.ui.activity.base.NavigationActivity

abstract class DrawerActivity(
    fragments: Map<Int, Fragment>,
    default: Int = 0
) : NavigationActivity(fragments, default, R.layout.activity_drawer) {
    private lateinit var binding: ActivityDrawerBinding
    private lateinit var selectedItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            navigationBar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigation.setNavigationItemSelectedListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            selectedItem.isChecked = false
            selectedItem = it
            it.isChecked = true
            onNavigationItemSelected(it)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        selectedItem = binding.navigation.menu[default]
        selectedItem.isChecked = true
        onNavigationItemSelected(selectedItem)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) binding.drawerLayout.closeDrawer(GravityCompat.START) else super.onBackPressed()
    }
}
