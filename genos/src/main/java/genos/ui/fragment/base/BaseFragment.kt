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

package genos.ui.fragment.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

abstract class BaseFragment(@LayoutRes val contentLayoutId: Int) : Fragment(contentLayoutId), DefaultLifecycleObserver {
    // 💖 Lifecycle
    // Android https://developer.android.com/guide/fragments/lifecycle

    // https://kotlinlang.org/docs/reference/classes.html
    // SO https://stackoverflow.com/questions/61306719/onactivitycreated-is-deprecated-how-to-properly-use-lifecycleobserver
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super<Fragment>.onCreate(savedInstanceState)
        onCreate(requireActivity().intent)
    }

//    override fun onDetach() {
//        activity?.lifecycle?.removeObserver(this)
//        super.onDetach()
//    }

    /**
     * 初始化 call, tileId, setHasOptionsMenu
     */
    protected abstract fun onCreate(intent: Intent)

    override fun onCreate(owner: LifecycleOwner) {
        activity?.lifecycle?.removeObserver(this)
    }

    override fun onOptionsItemSelected(item: MenuItem) = onPerform(item.itemId)

    protected abstract fun onPerform(action: Int): Boolean
}
