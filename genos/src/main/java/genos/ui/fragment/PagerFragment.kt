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

package genos.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.material.tabs.TabLayout

import java.util.ArrayList
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import genos.R

open class PagerFragment : Fragment() {
    protected lateinit var mTabLayout: TabLayout
    protected lateinit var mViewPager: ViewPager
    protected var mFragments = ArrayList<Fragment>()
    protected var mTitles = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pager, container, false)
        mTabLayout = view.findViewById(android.R.id.tabs)
        mViewPager = view.findViewById(R.id.pager)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // mViewPager.setPageMargin(16);
        // mViewPager.setPageMarginDrawable(android.R.color.black);
        mViewPager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getCount(): Int {
                return mFragments.size
            }

            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitles[position]
            }
        }
        mTabLayout.setupWithViewPager(mViewPager)
    }
}
