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

package com.example.genos.ui;

import android.content.Intent;

import com.example.genos.R;
import com.example.genos.models.User;

import genos.ui.fragment.TableList;
import genos.ui.viewholder.SubtitleHolder;

import static com.example.genos.AppManager.API;

public class UserList extends TableList<User, SubtitleHolder> {

    @Override
    protected void onPrepare() {
        mCall = API.userList(mPage);
        mTileId = R.layout.list_item_subtitle;
    }

    @Override
    protected void onDisplayItem(User item, SubtitleHolder holder, int viewType) {
        holder.title.setText(item.login);
        holder.subtitle.setText("id: " + item.id);
        holder.setImage(holder.icon, item.avatarUrl);
    }

    @Override
    protected void onOpenItem(User item) {
        Intent intent = new Intent();
        intent.putExtra("login", item.login);
        intent.setClass(requireContext(), UserDetailActivity.class);
        startActivitySafely(intent);
    }
}
