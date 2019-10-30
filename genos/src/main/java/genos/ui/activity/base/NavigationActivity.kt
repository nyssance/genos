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

package genos.ui.activity.base

import android.util.SparseArray
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nyssance.genos.R

abstract class NavigationActivity(val index: Int, contentLayoutId: Int) : BaseActivity(contentLayoutId) {
    protected val fragments = SparseArray<Fragment>()
    private var currentFragment: Fragment? = null
    private var currentTag = ""

    protected open fun onNavigationItemSelected(item: MenuItem): Boolean {
        val key = item.itemId
        val tag = item.toString()
        if (currentTag == tag) {
            return false
        }
        currentTag = tag
        val transaction = supportFragmentManager.beginTransaction()
        currentFragment?.let(transaction::hide)
        var fragment = supportFragmentManager.findFragmentByTag(currentTag) // 目标Fragment
        fragment?.let(transaction::show) ?: run {
            fragment = fragments[key]?.apply {
                transaction.add(R.id.container_for_add, this, currentTag)
            }
        }
        fragment?.let {
            // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.commit()
            currentFragment = it
        } ?: run {
            Toast.makeText(this, "Fragment R.id.${resources.getResourceEntryName(key)} not exists!", Toast.LENGTH_LONG).show()
        }
        return true
    }
}
