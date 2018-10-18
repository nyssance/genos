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

package genos.ui.fragment.base

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import genos.R
import genos.ui.BaseAdapter
import genos.ui.viewholder.BaseHolder
import java.lang.reflect.ParameterizedType

abstract class RecyclerViewFragment<D, T, VH : RecyclerView.ViewHolder> : LoaderFragment<D>() {
    protected var mListView: RecyclerView? = null
    protected lateinit var mLayoutManager: RecyclerView.LayoutManager
    protected lateinit var mAdapter: BaseAdapter<T, VH>
    @LayoutRes
    protected var mTileId = R.layout.list_item_default
    // FIXME: 找到只开启单选的方式
    internal var mSelectionPredicate: SelectionTracker.SelectionPredicate<T> = SelectionPredicates.createSelectSingleAnything()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recylcer_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListView = view.findViewById(android.R.id.list)
        mLayoutManager = onCreateLayoutManager(requireContext())
        mListView?.layoutManager = mLayoutManager
        mListView?.itemAnimator = DefaultItemAnimator()
        mAdapter = object : BaseAdapter<T, VH>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
                return this@RecyclerViewFragment.onCreateViewHolder(parent, viewType)
            }

            override fun onBindViewHolder(holder: VH, position: Int) {
                val item = getItem(position)
                if (item != null) {
                    onDisplayItem(item, holder, getItemViewType(position))
                } else {
                    Logger.t("recycler").w("item is null.")
                }
            }

            override fun getItemViewType(position: Int): Int {
                return onGetItemViewType(position)
            }
        }
        mListView?.adapter = mAdapter
        // SelectionTracker
        // Android: https://developer.android.com/guide/topics/ui/layout/recyclerview
        val tracker = SelectionTracker.Builder(
                "my-selection-id",
                mListView!!,
                mAdapter.keyProvider,
                mAdapter.detailsLookup,
                StorageStrategy.createLongStorage())
                .withOnItemActivatedListener({ item: ItemDetailsLookup.ItemDetails<T>, e: MotionEvent ->
                    this@RecyclerViewFragment.onItemClick(item.getSelectionKey())
                    Logger.w("单击 onItemActivated: ")
                    true
                })
                .withOnDragInitiatedListener({ e: MotionEvent ->
                    Logger.w("拖动 onDragInitiated: ")
                    true
                })
                .build()
        tracker.addObserver(object : SelectionTracker.SelectionObserver<T>() {
            override fun onItemStateChanged(key: T, selected: Boolean) {
                Logger.w("onItemStateChanged: ")
            }

            override fun onSelectionChanged() {
                Logger.w("多选 onSelectionChanged: ")
                var actionMode: ActionMode? = null
                if (tracker.hasSelection() && actionMode == null) {
                    actionMode = requireActivity().startActionMode(object : ActionMode.Callback {
                        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                            return false
                        }

                        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                            return false
                        }

                        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                            return false
                        }

                        override fun onDestroyActionMode(mode: ActionMode) {
                            tracker.clearSelection()
                        }
                    })
                    //                    setMenuItemTitle(tracker.getSelection().size());
                } else if (!tracker.hasSelection() && actionMode != null) {
                    actionMode.finish()
                    actionMode = null
                } else {
                    //                    setMenuItemTitle(tracker.getSelection().size());
                }
                //                for (T item : tracker.getSelection()) {
                //                    Logger.w(item.toString());
                //                }
            }
        })
    }

    override fun onDestroyView() {
        mListView = null
        super.onDestroyView()
    }

    /**
     * @param context
     * @return LayoutManager
     */
    protected abstract fun onCreateLayoutManager(context: Context): RecyclerView.LayoutManager

    /**
     * 获取子类的ViewHolder
     */
    protected open fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val superClassType = javaClass.getGenericSuperclass() as ParameterizedType
        val vhClass = superClassType.actualTypeArguments[1] as Class<VH>
        try {
            val constructor = vhClass.getDeclaredConstructor(View::class.java)
            return constructor.newInstance(LayoutInflater.from(parent.context).inflate(mTileId, parent, false))
        } catch (e: Exception) {
            Logger.t("recycler viewholder").e(e, "Exception")
        }
        val v = View(requireContext()) // TODO:
        return BaseHolder(v) as VH
    }

    protected fun onItemClick(item: T?) {
        onPerform(R.id.action_item_open, item)
    }

    override fun onPerform(action: Int): Boolean {
        return onPerform(action, null)
    }

    protected open fun onPerform(action: Int, item: T?): Boolean {
        if (action == R.id.action_item_open) {
            if (item != null) {
                onOpenItem(item)
            } else {
                Logger.t("recycler").w("item is null.")
            }
            return true
        } else if (action == R.id.action_view_refresh) {
            refresh()
            return true
        }
        return false
    }

    protected abstract fun onDisplayItem(item: T, holder: VH, viewType: Int)

    protected open fun onGetItemViewType(position: Int): Int {
        return 0
    }

    /**
     * Call on click list item
     *
     * @param item
     */
    protected abstract fun onOpenItem(item: T)

    // TODO:
    protected fun setSelection(position: Int, offset: Int) {
        mLayoutManager.scrollToPosition(position)
    }
}
