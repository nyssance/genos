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
import com.example.genos.API
import com.example.genos.R
import com.example.genos.model.User
import genos.extension.navigateTo
import genos.extension.setImage
import genos.ui.activity.AppBarActivity
import genos.ui.fragment.generic.List
import genos.ui.viewholder.Holder

class UserList : List<User, Holder>() {
    override fun onCreate(intent: Intent) {
        call = API.userList(page) // A retrofit call of this fragment.
        tileId = R.layout.list_item_subtitle // The layout res id of list item.
    }

    override fun onDisplayItem(item: User, view: Holder, viewType: Int) {
        with(view) {
            icon?.setImage(item.avatarUrl)
            title?.text = item.username
            subtitle?.text = item.id.toString()
        }
    }

    @ExperimentalStdlibApi
    override fun onOpenItem(item: User) {
        navigateTo(item.link)
    }
}

class UserListActivity : AppBarActivity() {
    override fun onCreateFragment() = UserList()
}
