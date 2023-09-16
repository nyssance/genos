/*
 * Copyright 2020 NY <nyssance@icloud.com>
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

import android.os.Bundle
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.nyssance.genos.R
import com.orhanobut.logger.Logger
import genos.ui.BaseAdapter
import genos.ui.viewholder.BaseHolder
import java.lang.reflect.ParameterizedType

abstract class RecyclerViewFragment<D : Any, T : Any, VH : RecyclerView.ViewHolder> : LoaderFragment<D>(0) {
    enum class ListViewStyle { Plain, Grouped }

    protected lateinit var listView: RecyclerView
    protected lateinit var adapter: BaseAdapter<T, VH>
    protected var listViewStyle = ListViewStyle.Plain
    protected var canSelectMultiple = false

    @LayoutRes
    protected var tileId = R.layout.list_item_default

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_recylcer_view, container, false)
        listView = view.findViewById(android.R.id.list)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = object : BaseAdapter<T, VH>() {
            override fun getItemViewType(position: Int) = onGetItemViewType(position)

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = this@RecyclerViewFragment.onCreateViewHolder(parent, viewType)

            override fun onBindViewHolder(holder: VH, position: Int) {
                if (listViewStyle == ListViewStyle.Grouped && adapter.sections.contains(position) && holder is BaseHolder) {
                    holder.setText(R.id.header_title, adapter.sections[position])
                }
                onDisplayItem(getItem(position), holder, getItemViewType(position))
            }
        }
        listView.itemAnimator = DefaultItemAnimator()
        listView.adapter = adapter
        // SelectionTracker
        // Android https://developer.android.com/guide/topics/ui/layout/recyclerview
        val tracker = SelectionTracker.Builder(
            "selection-id",
            listView, adapter.keyProvider, adapter.detailsLookup,
            StorageStrategy.createLongStorage()
        )
            .withSelectionPredicate(object : SelectionTracker.SelectionPredicate<Long>() {
                override fun canSetStateForKey(key: Long, nextState: Boolean) = canSelectMultiple

                override fun canSetStateAtPosition(position: Int, nextState: Boolean) = canSelectMultiple

                override fun canSelectMultiple() = canSelectMultiple
            })
            .withOnItemActivatedListener { item, _ ->
                onOpenItem(adapter.getItem(item.position))
                Logger.w("单击 onItemActivated: ")
                true
            }
            .withOnDragInitiatedListener {
                Logger.w("拖动 onDragInitiated: ")
                true
            }
            .build()
        tracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onItemStateChanged(key: Long, selected: Boolean) {
                Logger.w("onItemStateChanged: ")
            }

            override fun onSelectionChanged() {
                Logger.w("多选 onSelectionChanged: ")
                var actionMode: ActionMode? = null
                when {
                    tracker.hasSelection() && actionMode == null -> {
                        actionMode = activity?.startActionMode(object : ActionMode.Callback {
                            override fun onCreateActionMode(mode: ActionMode, menu: Menu) = false

                            override fun onPrepareActionMode(mode: ActionMode, menu: Menu) = false

                            override fun onActionItemClicked(mode: ActionMode, item: MenuItem) = false

                            override fun onDestroyActionMode(mode: ActionMode) {
                                tracker.clearSelection()
                            }
                        })
                        //                    setMenuItemTitle(tracker.selection.size())
                    }

                    !tracker.hasSelection() && actionMode != null -> {
                        actionMode.finish()
                    }

                    else -> {
                        //                    setMenuItemTitle(tracker.selection.size())
                    }
                }
                //                tracker.selection.forEach {
                //                    Logger.w(it.toString())
                //                }
            }
        })
    }

    /**
     * 获取子类的 ViewHolder
     *
     * @param parent
     * @param viewType
     *
     * @return VH
     */
    protected open fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val superClassType = javaClass.genericSuperclass as ParameterizedType
        superClassType.actualTypeArguments.lastOrNull()?.let {
            it as Class<VH>
            try {
                val constructor = it.getDeclaredConstructor(View::class.java)
                val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
                if (viewType == R.layout.list_section) {
                    setViewStub(view, R.id.stub, tileId)
                }
                return constructor.newInstance(view)
            } catch (cause: Throwable) {
                Logger.t(this::class.simpleName).e(cause, "Throwable")
            }
        }
        val v = View(context)
        return BaseHolder(v) as VH
    }

    final override fun onPerform(action: Int) = onPerform(action, null)

    protected open fun onPerform(action: Int, item: T?) = when (action) {
        R.id.action_item_open -> {
            item?.let(this::onOpenItem) ?: run {
                Logger.t(this::class.simpleName).w("item is null.")
            }
            true
        }

        R.id.action_view_refresh -> {
            refresh()
            true
        }

        else -> false
    }

    protected open fun onGetItemViewType(position: Int) = if (listViewStyle == ListViewStyle.Grouped && adapter.sections.contains(position)) R.layout.list_section else tileId

    protected abstract fun onDisplayItem(item: T, viewHolder: VH, viewType: Int)

    /**
     * Call on click list item
     *
     * @param item
     */
    protected abstract fun onOpenItem(item: T)

    protected fun setSelection(position: Int, offset: Int) {
        TODO("scroll offset")
        listView.layoutManager?.scrollToPosition(position)
    }

    protected fun setHeader(view: View, @LayoutRes layout: Int) = setViewStub(view, R.id.header, layout)

    protected fun setFooter(view: View, @LayoutRes layout: Int) = setViewStub(view, R.id.footer, layout)

    private fun setViewStub(view: View, @IdRes id: Int, @LayoutRes layout: Int) = view.findViewById<ViewStub>(id)?.run {
        layoutResource = layout
        inflate()
    }
}
