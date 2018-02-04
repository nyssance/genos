/*
 * Copyright 2018 NY (nyssance@icloud.com)
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

package genos.widget;

import android.support.v7.widget.RecyclerView;
import android.widget.SectionIndexer;

import genos.ui.BaseAdapter;

public abstract class IndexListAdapter<T, VH extends RecyclerView.ViewHolder> extends BaseAdapter<T, VH> implements SectionIndexer {
    private SectionIndexer mIndexer;

    public SectionIndexer getIndexer() {
        return mIndexer;
    }

    public void setIndexer(SectionIndexer indexer) {
        mIndexer = indexer;
    }

    @Override
    public Object[] getSections() {
        return mIndexer == null ? new String[]{" "} : mIndexer.getSections();
    }

    @Override
    public int getPositionForSection(int section) {
        return mIndexer == null ? -1 : mIndexer.getPositionForSection(section);
    }

    @Override
    public int getSectionForPosition(int position) {
        return mIndexer == null ? -1 : mIndexer.getSectionForPosition(position);
    }
}
