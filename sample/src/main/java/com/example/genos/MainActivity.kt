/*
 * Copyright 2018 NY <nyssance@icloud.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.genos

import android.os.Bundle
import com.example.genos.ui.Discover
import com.example.genos.ui.UserList
import genos.ui.activity.TabBarActivity
import genos.ui.fragment.PlaceholderFragment

class MainActivity : TabBarActivity(1) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        config()
        supportActionBar?.setTitle(R.string.app_name)
        with(fragments) {
            append(R.id.navigation_home, UserList())
            append(R.id.navigation_discover, Discover())
            append(R.id.navigation_me, PlaceholderFragment.instance("3"))
        }
    }
}
