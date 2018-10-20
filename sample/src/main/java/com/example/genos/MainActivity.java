/*
 * Copyright 2018 NY <nyssance@icloud.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.genos;

import android.os.Bundle;

import com.example.genos.ui.UserList;

import org.jetbrains.annotations.Nullable;

import genos.ui.activity.TabBarActivity;
import genos.ui.fragment.PlaceholderFragment;

public class MainActivity extends TabBarActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments.append(R.id.navigation_home, new UserList());
        fragments.append(R.id.navigation_discover, PlaceholderFragment.newInstance(2));
        fragments.append(R.id.navigation_me, PlaceholderFragment.newInstance(3));
        onNavigationItemSelected(R.id.navigation_home);
    }
}
