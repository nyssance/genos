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

//import androidx.ui.core.Alignment.Companion.CenterVertically
//import androidx.ui.core.Modifier
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Recomposer
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.platform.setContent
import com.example.genos.R
import com.example.genos.model.User
import genos.extension.showActionSheet
import genos.model.Item
import genos.ui.fragment.generic.Detail

class Discover : Detail<User>() {
    override fun onCreate(intent: Intent, id: String) {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            super.onCreateView(inflater, container, savedInstanceState).apply {
                (this as ViewGroup).setContent(Recomposer.current()) {
                    Discover("Android")
                }
            }

    override fun onDisplay(data: User) {
        showActionSheet("aaa", arrayListOf(Item(context, R.string.home, "1", "2"))) {}
    }
}

@Composable
fun Discover(name: String) {
    TableView(items)
}

val items = mapOf(
        "Android" to listOf("Jetpack Compose", "Kotlin", "Jetpack"),
        "Programming" to listOf("Kotlin", "Declarative UIs", "Java"),
        "Technology" to listOf("Pixel", "Google")
)

@Composable
private fun TableView(items: Map<String, List<String>>) {
//    VerticalScroller {
//        Column {
//            items.forEach { (section, topics) ->
//                Text(section, Modifier.padding(16.dp), style = MaterialTheme.typography.subtitle1)
//                topics.forEach {
//                    ListItem(imageResource(R.drawable.placeholder_1_1), it)
//                    Divider(Modifier.padding(72.dp, 8.dp, 0.dp, 8.dp), MaterialTheme.colors.surface.copy(0.08f))
//                }
//            }
//        }
//    }
}

@Composable
private fun ListItem(icon: ImageAsset?, title: String, subtitle: String = "") {
    val onSelected = { it: Boolean ->
    }
//    Toggleable(true, onSelected, modifier = Modifier.ripple()) {
//        Row(Modifier.padding(start = 16.dp, end = 16.dp)) {
//            icon?.let {
//                Image(it, Modifier.gravity(CenterVertically).preferredSize(56.dp, 56.dp).clip(RoundedCornerShape(4.dp)))
//            }
//            Text(title, Modifier.weight(1f).gravity(CenterVertically).padding(16.dp), style = MaterialTheme.typography.subtitle1)
////            SelectTopicButton(
////                    modifier = Modifier.gravity(CenterVertically),
////                    selected = selected
////            )
//        }
//    }
}
