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

package genos.widget

import android.widget.SectionIndexer

import androidx.recyclerview.widget.RecyclerView
import genos.ui.BaseAdapter

abstract class IndexListAdapter<T, VH : RecyclerView.ViewHolder> : BaseAdapter<T, VH>(), SectionIndexer {
    lateinit var indexer: SectionIndexer

    override fun getSections(): Array<Any> {
        return if (indexer == null) arrayOf<Any>(" ") else indexer.sections
    }

    override fun getPositionForSection(section: Int): Int {
        return if (indexer == null) -1 else indexer.getPositionForSection(section)
    }

    override fun getSectionForPosition(position: Int): Int {
        return if (indexer == null) -1 else indexer.getSectionForPosition(position)
    }
}
