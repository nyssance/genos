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
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import genos.R

abstract class NavigationActivity : BaseActivity() {
    protected var mFragments = SparseArray<Fragment>()
    private var mCurrentFragment: Fragment? = null
    private var mCurrentTag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    protected fun onNavigationItemSelected(@IdRes id: Int): Boolean {
        val tag = String.format("%s", id)
        if (mCurrentTag == tag) {
            return false
        }
        mCurrentTag = tag
        val transaction = supportFragmentManager.beginTransaction()
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment!!)
        }
        var fragment = supportFragmentManager.findFragmentByTag(mCurrentTag) // 目标Fragment
        if (fragment == null) {
            fragment = mFragments.get(id)
            transaction.add(R.id.container_for_add, fragment!!, mCurrentTag)
        } else {
            transaction.show(fragment)
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
        mCurrentFragment = fragment
        return true
    }
}
