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
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import genos.R
import genos.ui.BaseAdapter
import genos.ui.viewholder.BaseHolder
import java.lang.reflect.ParameterizedType

abstract class RecyclerViewFragment<D : Any, T : Any, VH : RecyclerView.ViewHolder> : LoaderFragment<D>() {
    protected lateinit var listView: RecyclerView
    protected lateinit var layoutManager: RecyclerView.LayoutManager
    protected lateinit var adapter: BaseAdapter<T, VH>
    @JvmField
    @LayoutRes
    protected var tileId = R.layout.list_item_default
    @JvmField
    protected var canSelectMultiple = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recylcer_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById(android.R.id.list)
        layoutManager = onCreateLayoutManager(requireContext())
        listView.layoutManager = layoutManager
        listView.itemAnimator = DefaultItemAnimator()
        adapter = object : BaseAdapter<T, VH>() {
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
        listView.adapter = adapter
        // SelectionTracker
        // Android: https://developer.android.com/guide/topics/ui/layout/recyclerview
        val tracker = SelectionTracker.Builder<Long>(
                "my-selection-id",
                listView,
                adapter.keyProvider,
                adapter.detailsLookup,
                StorageStrategy.createLongStorage()
        )
                .withSelectionPredicate(object : SelectionTracker.SelectionPredicate<Long>() {
                    override fun canSetStateForKey(key: Long, nextState: Boolean): Boolean {
                        return canSelectMultiple
                    }

                    override fun canSetStateAtPosition(position: Int, nextState: Boolean): Boolean {
                        return canSelectMultiple
                    }

                    override fun canSelectMultiple(): Boolean {
                        return canSelectMultiple
                    }

                })
                .withOnItemActivatedListener { item: ItemDetailsLookup.ItemDetails<Long>, e: MotionEvent ->
                    this@RecyclerViewFragment.onOpenItem(adapter.getItem(item.position))
                    Logger.w("单击 onItemActivated: ")
                    true
                }
                .withOnDragInitiatedListener { e: MotionEvent ->
                    Logger.w("拖动 onDragInitiated: ")
                    true
                }
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
                    //                    setMenuItemTitle(tracker.selection.size())
                } else if (!tracker.hasSelection() && actionMode != null) {
                    actionMode.finish()
                    actionMode = null
                } else {
                    //                    setMenuItemTitle(tracker.selection.size())
                }
                //                for (T item : tracker.getSelection()) {
                //                    Logger.w(item)
                //                }
            }
        })
    }

//    override fun onDestroyView() {
//        mListView = null
//        super.onDestroyView()
//    }

    /**
     * @param context
     * @return LayoutManager
     */
    protected abstract fun onCreateLayoutManager(context: Context): RecyclerView.LayoutManager

    /**
     * 获取子类的ViewHolder
     */
    protected open fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val superClassType = javaClass.genericSuperclass as ParameterizedType
        val vhClass = superClassType.actualTypeArguments[1] as Class<VH>
        try {
            val constructor = vhClass.getDeclaredConstructor(View::class.java)
            return constructor.newInstance(LayoutInflater.from(parent.context).inflate(tileId, parent, false))
        } catch (e: Exception) {
            Logger.t("recycler viewholder").e(e, "Exception")
        }
        val v = View(requireContext()) // TODO:
        return BaseHolder(v) as VH
    }

    override fun onPerform(action: Int): Boolean {
        return onPerform(action, null)
    }

    protected open fun onPerform(action: Int, item: T?): Boolean {
        return when (action) {
            R.id.action_item_open -> {
                if (item != null) {
                    onOpenItem(item)
                } else {
                    Logger.t("recycler").w("item is null.")
                }
                true
            }
            R.id.action_view_refresh -> {
                refresh()
                return true
            }
            else -> false
        }
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
        layoutManager.scrollToPosition(position)
    }
}
