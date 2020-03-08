/*
 * Copyright 2020 NY <nyssance@icloud.com>
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

import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import com.example.genos.ui.Discover
import com.example.genos.ui.Home
import com.example.genos.ui.Me
import com.example.genos.ui.Message
import genos.Global
import genos.ui.activity.TabBarActivity

class MainActivity : TabBarActivity(mapOf(
        R.id.navigation_1 to Home(),
        R.id.navigation_2 to Discover(),
        R.id.navigation_3 to Message(),
        R.id.navigation_4 to Me()
)) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(Global) {
            APP_SCHEME = "genos-sample"
        }
        router()
        supportActionBar?.setTitle(R.string.app_name)
        if (intent.action == Intent.ACTION_MAIN && intent.data == null) {
            intent.data = "${Global.APP_SCHEME}://discover".toUri()
        }
    }
}
