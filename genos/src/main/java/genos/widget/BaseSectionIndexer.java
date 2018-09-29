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

package genos.widget;

import android.widget.SectionIndexer;

import java.util.Arrays;

/**
 * 该类参考官方Contacts的ContactsSectionIndexer
 */
public class BaseSectionIndexer implements SectionIndexer {
    private String[] mSections;
    private int[] mPositions;
    private int mCount;

    public BaseSectionIndexer(String[] sections, int[] positions, int count) {
        mSections = sections;
        mPositions = positions;
        mCount = count;
    }

    @Override
    public Object[] getSections() {
        return mSections;
    }

    @Override
    public int getPositionForSection(int section) {
        if (section < 0 || section >= mSections.length) {
            return -1;
        }
        return mPositions[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        if (position < 0 || position >= mCount) {
            return -1;
        }
        // mPositions的数据必须是排序好的，二分法查找必须有序
        int index = Arrays.binarySearch(mPositions, position);
        return index >= 0 ? index : -index - 2;
    }
}
