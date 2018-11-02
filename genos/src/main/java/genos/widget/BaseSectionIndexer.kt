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
import java.util.*

/**
 * 该类参考官方Contacts的ContactsSectionIndexer
 */
class BaseSectionIndexer(private val mSections: Array<Any>, private val mPositions: IntArray, private val mCount: Int) :
    SectionIndexer {

    override fun getSections(): Array<Any> {
        return mSections
    }

    override fun getPositionForSection(section: Int): Int {
        return if (section < 0 || section >= mSections.size) {
            -1
        } else mPositions[section]
    }

    override fun getSectionForPosition(position: Int): Int {
        if (position < 0 || position >= mCount) {
            return -1
        }
        // mPositions的数据必须是排序好的，二分法查找必须有序
        val index = Arrays.binarySearch(mPositions, position)
        return if (index >= 0) index else -index - 2
    }
}
