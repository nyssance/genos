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
import com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED
import genos.R
import genos.ui.activity.base.NavigationActivity
import kotlinx.android.synthetic.main.activity_tab_bar.*

abstract class TabBarActivity(var index: Int = 0) : NavigationActivity() {
    override fun onSetContentView(name: String) {
        setContentView(R.layout.activity_tab_bar)
        navigation.labelVisibilityMode = LABEL_VISIBILITY_LABELED
        navigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        navigation.selectedItemId = navigation.menu.get(index).itemId
    }
}
