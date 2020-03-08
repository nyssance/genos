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

package com.example.genos.ui

import android.content.Intent
import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.layout.LayoutPadding
import androidx.ui.layout.Table
import androidx.ui.unit.dp
import com.example.genos.model.User
import com.nyssance.genos.R
import genos.extension.showActionSheet
import genos.model.Item
import genos.ui.fragment.generic.Detail

class Discover : Detail<User>() {
    override fun onCreate(intent: Intent, id: String) {}

    @Composable
    override fun onCompose() {
        Discover("Android")
    }

    override fun onDisplay(data: User) {
        showActionSheet("aaa", arrayListOf(Item(requireContext(), R.string.home, "1", "2"))) {}
    }
}

@Composable
fun Discover(name: String) {
    Table(8) {
        repeat(8) {
            tableRow {
                repeat(8) {
                    Text("TX", LayoutPadding(2.dp, 2.dp, 2.dp, 2.dp))
                }
            }
        }
    }
}
