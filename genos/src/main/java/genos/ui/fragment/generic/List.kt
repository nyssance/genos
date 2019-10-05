/*
 * Copyright 2018 NY <nyssance@icloud.com>
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

package genos.ui.fragment.generic

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import genos.ui.fragment.base.ListFragment

abstract class List<T : Any, VH : RecyclerView.ViewHolder> : ListFragment<ArrayList<T>, T, VH>(1) {
    // https://kotlinlang.org/docs/reference/classes.html
    override fun onUpdateLayoutManager() {
        listView.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
    }

    final override fun transformListFromData(data: ArrayList<T>): ArrayList<T> {
        return data
    }
}
