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

package genos.ui;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private List<T> mData = new ArrayList<>();
    private ItemKeyProvider<Long> mKeyProvider;
    private ItemDetailsLookup<Long> mDetailsLookup;

    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        // KeyProvider & DetailsLookup
        mKeyProvider = new ItemKeyProvider<Long>(ItemKeyProvider.SCOPE_MAPPED) {
            @Nullable
            @Override
            public Long getKey(int position) {
                return (long) position;
            }

            @Override
            public int getPosition(@NonNull Long key) {
                return key.intValue();
            }
        };
        mDetailsLookup = new ItemDetailsLookup<Long>() {
            @Nullable
            @Override
            public ItemDetails getItemDetails(@NonNull MotionEvent e) {
                View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (view != null) {
                    RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
                    final int position = holder.getAdapterPosition();
                    return new ItemDetails<Long>() {
                        @Override
                        public int getPosition() {
                            return position;
                        }

                        @Nullable
                        @Override
                        public Long getSelectionKey() {
                            return (long) position;
                        }
                    };
                }
                return null;
            }
        };
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @NonNull
    public final ItemKeyProvider<Long> getKeyProvider() {
        return mKeyProvider;
    }

    @NonNull
    public final ItemDetailsLookup<Long> getDetailsLookup() {
        return mDetailsLookup;
    }

    @Nullable
    public final T getItem(int position) {
        return mData.get(position);
    }

    public final void append(@NonNull Collection<T> items) {
        mData.addAll(items);
    }

    public final void removeAll() {
        mData.clear();
    }

    public void add(int index, T item) {
        mData.add(index, item);
        notifyItemInserted(index); // TODO: 同步调用会不会出错
    }

    public void remove(int index) {
        mData.remove(index);
        notifyItemRemoved(index);
    }
}
