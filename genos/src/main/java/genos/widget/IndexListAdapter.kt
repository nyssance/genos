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

abstract class IndexListAdapter<T : Any, VH : RecyclerView.ViewHolder> : BaseAdapter<T, VH>(), SectionIndexer {
    var indexer: SectionIndexer? = null

    override fun getSections(): Array<Any> {
        indexer?.let {
            return it.sections
        }
        return arrayOf(" ")
    }

    override fun getPositionForSection(section: Int): Int {
        indexer?.let {
            return it.getPositionForSection(section)
        }
        return -1
    }

    override fun getSectionForPosition(position: Int): Int {
        indexer?.let {
            return it.getSectionForPosition(position)
        }
        return -1
    }
}
