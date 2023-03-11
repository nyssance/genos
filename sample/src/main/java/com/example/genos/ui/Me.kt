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
import com.example.genos.R
import com.example.genos.model.User
import genos.model.Item
import genos.ui.fragment.generic.TableViewDetail
import genos.ui.viewholder.Holder

class Me : TableViewDetail<User, Item, Holder>() {
    override fun onCreate(intent: Intent) {
        items.addAll(
            listOf(
                Item(context, R.string.home, link = "https://www.baidu.com"),
                Item(context, R.string.help, link = "https://docs.wechatpy.org/zh_CN/stable/"),
                Item(context, R.string.settings, link = "settings"),
                Item(context, R.string.web)
            )
        )
    }
}
