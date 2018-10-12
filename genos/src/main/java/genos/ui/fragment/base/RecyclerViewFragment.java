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

package genos.ui.fragment.base;

import android.content.Context;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.OnItemActivatedListener;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import genos.R;
import genos.ui.BaseAdapter;

public abstract class RecyclerViewFragment<D, T, VH extends RecyclerView.ViewHolder> extends LoaderFragment<D> {
    protected RecyclerView mListView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected BaseAdapter<T, VH> mAdapter;
    @LayoutRes
    protected int mTileId = R.layout.list_item_default;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recylcer_view, container, false);
    }

    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = view.findViewById(android.R.id.list);
        mLayoutManager = onCreateLayoutManager(requireContext());
        mListView.setLayoutManager(mLayoutManager);
        mListView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new BaseAdapter<T, VH>() {
            @NonNull
            @Override
            public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return RecyclerViewFragment.this.onCreateViewHolder(parent, viewType);
            }

            @Override
            public void onBindViewHolder(@NonNull VH holder, int position) {
                T item = getItem(position);
                if (item != null) {
                    onDisplayItem(item, holder, getItemViewType(position));
                } else {
                    Logger.t("recycler").w("item is null.");
                }
            }

            @Override
            public int getItemViewType(int position) {
                return onGetItemViewType(position);
            }
        };
        mListView.setAdapter(mAdapter);
        // SelectionTracker
        SelectionTracker<T> selectionTracker = new SelectionTracker.Builder<>(
                "my-selection-id",
                mListView,
                mAdapter.getKeyProvider(),
                mAdapter.getDetailsLookup(),
                StorageStrategy.createLongStorage())
                .withOnItemActivatedListener((OnItemActivatedListener<T>) (item, e) -> {
                    RecyclerViewFragment.this.onItemClick(item.getSelectionKey());
                    Logger.w("单击 onItemActivated: ");
                    return true;
                })
                .withOnDragInitiatedListener(e -> {
                    Logger.w("拖动 onDragInitiated: ");
                    return true;
                })
                .build();
        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<T>() {
            @Override
            public void onItemStateChanged(@NonNull T key, boolean selected) {
            }

            @Override
            public void onSelectionChanged() {
                Logger.w("多选 onSelectionChanged: ");
                ActionMode actionMode = null;
                if (selectionTracker.hasSelection() && actionMode == null) {
                    actionMode = requireActivity().startActionMode(new ActionMode.Callback() {
                        @Override
                        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                            return false;
                        }

                        @Override
                        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                            return false;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            selectionTracker.clearSelection();
                        }
                    });
//                    setMenuItemTitle(selectionTracker.getSelection().size());
                } else if (!selectionTracker.hasSelection() && actionMode != null) {
                    actionMode.finish();
                    actionMode = null;
                } else {
//                    setMenuItemTitle(selectionTracker.getSelection().size());
                }
                for (T item : selectionTracker.getSelection()) {
                    Logger.w(item.toString());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        mListView = null;
        super.onDestroyView();
    }

    /**
     * @param context
     * @return LayoutManager
     */
    @NonNull
    protected abstract RecyclerView.LayoutManager onCreateLayoutManager(@NonNull Context context);

    /**
     * 获取子类的ViewHolder
     */
    @Nullable
    protected VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ParameterizedType superClassType = (ParameterizedType) getClass().getGenericSuperclass();
        Class<VH> vhClass = (Class<VH>) superClassType.getActualTypeArguments()[1];
        try {
            Constructor<VH> constructor = vhClass.getDeclaredConstructor(View.class);
            return constructor.newInstance(LayoutInflater.from(parent.getContext()).inflate(mTileId, parent, false));
        } catch (Exception e) {
            Logger.t("recycler viewholder").e(e, "Exception");
        }
        return null;
    }

    protected void onItemClick(@Nullable T item) {
        onPerform(R.id.action_item_open, item);
    }

    @Override
    protected final boolean onPerform(int action) {
        return onPerform(action, null);
    }

    protected boolean onPerform(int action, @Nullable T item) {
        if (action == R.id.action_item_open) {
            if (item != null) {
                onOpenItem(item);
            } else {
                Logger.t("recycler").w("item is null.");
            }
            return true;
        } else if (action == R.id.action_view_refresh) {
            refresh();
            return true;
        }
        return false;
    }

    protected abstract void onDisplayItem(@NonNull T item, @NonNull VH holder, int viewType);

    protected int onGetItemViewType(int position) {
        return 0;
    }

    /**
     * Call on click list item
     *
     * @param item
     */
    protected abstract void onOpenItem(@NonNull T item);

    // TODO:
    protected void setSelection(int position, int offset) {
        mLayoutManager.scrollToPosition(position);
    }
}
