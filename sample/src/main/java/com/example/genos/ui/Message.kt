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

package com.example.genos.ui

import genos.ui.fragment.PlaceholderFragment
import genos.ui.fragment.ViewPagerFragment

class Message : ViewPagerFragment(arrayListOf(
        "tab 1" to PlaceholderFragment.instance("1"),
        "tab 2" to PlaceholderFragment.instance("2"),
        "tab 3" to PlaceholderFragment.instance("3")
))
