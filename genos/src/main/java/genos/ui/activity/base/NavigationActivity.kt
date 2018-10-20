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

import android.os.Bundle
import android.util.SparseArray
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import genos.R

abstract class NavigationActivity : BaseActivity() {
    protected var fragments = SparseArray<Fragment>()
    private var currentFragment: Fragment? = null
    private var currentTag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    protected open fun onNavigationItemSelected(@IdRes id: Int): Boolean {
        val tag = id.toString()
        if (currentTag == tag) {
            return false
        }
        currentTag = tag
        val transaction = supportFragmentManager.beginTransaction()
        if (currentFragment != null) {
            transaction.hide(currentFragment!!)
        }
        var fragment = supportFragmentManager.findFragmentByTag(currentTag) // 目标Fragment
        if (fragment == null) {
            fragment = fragments.get(id)
            transaction.add(R.id.container_for_add, fragment!!, currentTag)
        } else {
            transaction.show(fragment)
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
        currentFragment = fragment
        return true
    }
}
