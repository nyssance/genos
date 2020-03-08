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
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED
import com.nyssance.genos.R
import genos.ui.activity.base.NavigationActivity
import kotlinx.android.synthetic.main.activity_tab_bar.*

abstract class TabBarActivity(
        fragments: Map<Int, Fragment>,
        index: Int = 0
) : NavigationActivity(fragments, index, R.layout.activity_tab_bar) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigation.labelVisibilityMode = LABEL_VISIBILITY_LABELED
        navigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        navigation.selectedItemId = navigation.menu[index].itemId
    }
}
