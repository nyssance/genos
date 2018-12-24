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
import com.example.genos.AppManager.Companion.API
import com.example.genos.R
import com.example.genos.models.User
import genos.startActivitySafely
import genos.ui.fragment.TableList
import genos.ui.viewholder.SubtitleHolder

class UserList : TableList<User, SubtitleHolder>() {
    override fun onPrepare() {
        call = API.userList(page)  // a retrofit call of this fragment.
        tileId = R.layout.list_item_subtitle  // the layout res id of list item.
    }

    override fun onDisplayItem(item: User, holder: SubtitleHolder, viewType: Int) {
        holder.title.text = item.login
        holder.subtitle.text = item.id.toString()
        item.avatarUrl?.let {
            holder.setImage(holder.icon, it)
        }
    }

    override fun onOpenItem(item: User) {
        val intent = Intent()
        intent.putExtra("login", item.login)
        intent.setClass(requireContext(), UserDetailActivity::class.java)
        startActivitySafely(intent)
    }
}
