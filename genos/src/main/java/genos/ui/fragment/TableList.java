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

package genos.ui.fragment;

import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import genos.ui.fragment.base.ListFragment;

public abstract class TableList<T, VH extends RecyclerView.ViewHolder> extends ListFragment<List<T>, T, VH> {

    @NonNull
    @Override
    protected final RecyclerView.LayoutManager onCreateLayoutManager(@NonNull Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mListView.addItemDecoration(new DividerItemDecoration(context, layoutManager.getOrientation()));
        return layoutManager;
    }

    @NonNull
    @Override
    protected final List<T> transformListFromData(@NonNull List<T> data) {
        return data;
    }
}
