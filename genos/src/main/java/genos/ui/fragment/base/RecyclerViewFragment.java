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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import genos.R;
import genos.ui.BaseAdapter;
import genos.widget.AbsRecyclerView;
import genos.widget.recycler.RecyclerItemClickListener;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE_MODAL;

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
        mListView.addOnItemTouchListener(new RecyclerItemClickListener(requireContext(), mListView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mListView instanceof AbsRecyclerView) {
                    AbsRecyclerView absRecyclerView = (AbsRecyclerView) mListView;
                    if (absRecyclerView.getChoiceMode() == CHOICE_MODE_MULTIPLE_MODAL && absRecyclerView.getCheckedItemCount() > 0) {
                        absRecyclerView.setItemChecked(position, !absRecyclerView.isItemChecked(position));
                        if (absRecyclerView.getCheckedItemCount() == 0) {
                            absRecyclerView.clearChoices();
                        }
                    } else {
                        RecyclerViewFragment.this.onItemClick(mAdapter.getItem(position));
                    }
                } else {
                    RecyclerViewFragment.this.onItemClick(mAdapter.getItem(position));
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (mListView instanceof AbsRecyclerView) {
                    AbsRecyclerView absRecyclerView = (AbsRecyclerView) mListView;
                    if (absRecyclerView.getChoiceMode() == CHOICE_MODE_MULTIPLE_MODAL) {
                        requireActivity().startActionMode(absRecyclerView.getMultiChoiceModeListener());
                        absRecyclerView.performLongPress(position);
                    }
                }
            }
        }));
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
