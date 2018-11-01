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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import genos.R

open class PagerFragment : Fragment() {
    protected lateinit var tabLayout: TabLayout
    protected lateinit var viewPager: ViewPager
    protected var fragments = ArrayList<Fragment>()
    protected var titles = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pager, container, false)
        tabLayout = view.findViewById(android.R.id.tabs)
        viewPager = view.findViewById(R.id.pager)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // viewPager.setPageMargin(16);
        // viewPager.setPageMarginDrawable(android.R.color.black);
        viewPager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getCount(): Int {
                return fragments.size
            }

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return titles[position]
            }
        }
        tabLayout.setupWithViewPager(viewPager)
    }
}
