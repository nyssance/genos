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
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.util.contains
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.nyssance.genos.R
import com.orhanobut.logger.Logger
import genos.ui.BaseAdapter
import genos.ui.viewholder.BaseHolder
import java.lang.reflect.ParameterizedType

enum class ListViewStyle {
    Plain, Grouped
}

abstract class RecyclerViewFragment<D : Any, T : Any, VH : RecyclerView.ViewHolder> : LoaderFragment<D>() {
    protected lateinit var listView: RecyclerView
    protected lateinit var layoutManager: RecyclerView.LayoutManager
    protected lateinit var adapter: BaseAdapter<T, VH>
    @JvmField
    protected var listViewStyle = ListViewStyle.Plain
    @JvmField
    protected var canSelectMultiple = false

    @JvmField
    @LayoutRes
    protected var tileId = R.layout.list_item_default

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
                if (listViewStyle == ListViewStyle.Grouped && adapter.sections.contains(position) && holder is BaseHolder) {
                    holder.setText(R.id.header_title, adapter.sections[position])
                }
                onDisplayItem(getItem(position), holder, getItemViewType(position))
            }

            override fun getItemViewType(position: Int): Int {
                return onGetItemViewType(position)
            }
        }
        listView.adapter = adapter
        // NY: 不设为 false 的话, 在有NestedScrollView时, 滚动不平滑, 没有上下边界处水波效果; 无NestedScrollView设为 true/false 无影响
        listView.isNestedScrollingEnabled = false
        // SelectionTracker
        // Android: https://developer.android.com/guide/topics/ui/layout/recyclerview
        val tracker = SelectionTracker.Builder(
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
                .withOnItemActivatedListener { item, _ ->
                    this@RecyclerViewFragment.onOpenItem(adapter.getItem(item.position))
                    Logger.w("单击 onItemActivated: ")
                    true
                }
                .withOnDragInitiatedListener {
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
                } else {
                    //                    setMenuItemTitle(tracker.selection.size())
                }
                //                tracker.selection.forEach {
                //                    Logger.w(it.toString())
                //                }
            }
        })
    }

    /**
     * @param context
     * @return LayoutManager
     */
    protected abstract fun onCreateLayoutManager(context: Context): RecyclerView.LayoutManager

    /**
     * 获取子类的 ViewHolder
     */
    protected open fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val superClassType = javaClass.genericSuperclass as ParameterizedType
        superClassType.actualTypeArguments.lastOrNull()?.let {
            it as Class<VH>
            try {
                val constructor = it.getDeclaredConstructor(View::class.java)
                val resource = viewType
                val view = LayoutInflater.from(parent.context).inflate(resource, parent, false)
                if (resource == R.layout.list_section) {
                    setViewStub(view, R.id.stub, tileId)
                }
                return constructor.newInstance(view)
            } catch (e: Exception) {
                Logger.t("recycler viewholder").e(e, "Exception")
            }
        }
        val v = View(requireContext())
        return BaseHolder(v) as VH
    }

    final override fun onPerform(action: Int): Boolean {
        return onPerform(action, null)
    }

    protected open fun onPerform(action: Int, item: T?): Boolean {
        return when (action) {
            R.id.action_item_open -> {
                item?.let(this::onOpenItem) ?: run {
                    Logger.t("recycler").w("item is null.")
                }
                true
            }
            R.id.action_view_refresh -> {
                refresh()
                true
            }
            else -> false
        }
    }

    protected abstract fun onDisplayItem(item: T, holder: VH, viewType: Int)

    protected open fun onGetItemViewType(position: Int): Int {
        return if (listViewStyle == ListViewStyle.Grouped && adapter.sections.contains(position)) R.layout.list_section else tileId
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

    protected fun setHeader(view: View, @LayoutRes layout: Int): View? {
        return setViewStub(view, R.id.header, layout)
    }

    protected fun setFooter(view: View, @LayoutRes layout: Int): View? {
        return setViewStub(view, R.id.footer, layout)
    }

    private fun setViewStub(view: View, @IdRes id: Int, @LayoutRes layout: Int): View? {
        return view.findViewById<ViewStub>(id)?.run {
            layoutResource = layout
            inflate()
        }
    }
}
