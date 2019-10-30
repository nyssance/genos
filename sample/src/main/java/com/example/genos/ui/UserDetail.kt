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

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.genos.API
import com.example.genos.model.User
import genos.ui.activity.CollapsingActivity
import genos.ui.fragment.generic.Detail

class UserDetail : Detail<User>() {
    private var textView: TextView? = null

    override fun onCreate() {
        call = API.userDetail(requireActivity().intent.getStringExtra("username").orEmpty())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView = view.findViewById(android.R.id.text1)
        textView?.text = ""
//        text1.text = ""
    }

    override fun onDisplay(data: User) {
        (activity as UserDetailActivity).collapsingToolbar?.title = data.username
        textView?.text = data.id.toString()
    }
}

class UserDetailActivity : CollapsingActivity() {
    override fun onCreateFragment(): Fragment {
        return UserDetail()
    }
}
