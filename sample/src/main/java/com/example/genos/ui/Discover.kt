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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.example.genos.R
import com.example.genos.model.User
import genos.extension.showActionSheet
import genos.model.Item
import genos.ui.fragment.generic.Detail

class Discover : Detail<User>(0) {

    override fun onCreate(intent: Intent, id: String) = Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Discover("Android")
            }
        }
    }

    override fun onDisplay(data: User) {
        showActionSheet("aaa", arrayListOf(Item(context, R.string.home, "1", "2"))) {}
    }
}

@Composable
fun Discover(name: String) {
    val paddingModifier = Modifier.padding(10.dp)
    Card(modifier = paddingModifier) {
        Column {
            Text(name, style = MaterialTheme.typography.headlineMedium)
            Text("Text with card content color (Blue)", paddingModifier, Color.Blue)
        }
    }
}
