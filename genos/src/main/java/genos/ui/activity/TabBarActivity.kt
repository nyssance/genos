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
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView.LABEL_VISIBILITY_LABELED
import com.google.android.material.navigation.NavigationBarView.LabelVisibility
import com.nyssance.genos.R
import com.nyssance.genos.databinding.ActivityTabBarBinding
import genos.ui.activity.base.NavigationActivity

abstract class TabBarActivity(
    fragments: Map<Int, Fragment>,
    default: Int = 0,
    @LabelVisibility private val mode: Int = LABEL_VISIBILITY_LABELED
) : NavigationActivity(fragments, default, R.layout.activity_tab_bar) {
    private lateinit var binding: ActivityTabBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabBarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.navigation.labelVisibilityMode = mode
        binding.navigation.setOnItemSelectedListener(this::onNavigationItemSelected)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        binding.navigation.selectedItemId = binding.navigation.menu[default].itemId
    }
}
