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

package genos.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nyssance.genos.R

abstract class ViewPager(
    private val fragments: ArrayList<Pair<String, Fragment>>,
    private val orientation: Int = ViewPager2.ORIENTATION_HORIZONTAL
) : Fragment(R.layout.fragment_view_pager) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
        viewPager.orientation = orientation
        viewPager.adapter = object : FragmentStateAdapter(requireActivity()) {
            override fun getItemCount() = fragments.size

            override fun createFragment(position: Int) = fragments[position].component2()
        }
        view.findViewById<TabLayout>(android.R.id.tabs)?.let {
            TabLayoutMediator(it, viewPager) { tab, position ->
                tab.text = fragments[position].component1()
            }.attach()
        }
    }
}
