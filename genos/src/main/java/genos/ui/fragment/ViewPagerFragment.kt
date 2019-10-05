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
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nyssance.genos.R
import kotlinx.android.synthetic.main.fragment_view_pager.*

abstract class ViewPagerFragment(private val orientation: Int = ViewPager2.ORIENTATION_HORIZONTAL) : Fragment() {
    @JvmField
    protected var fragments = ArrayList<Pair<String, Fragment>>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pager.orientation = orientation
        pager.adapter = object : FragmentStateAdapter(requireActivity()) {
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position].second
            }
        }
        requireActivity().findViewById<TabLayout>(android.R.id.tabs)?.let {
            it.visibility = View.VISIBLE
            TabLayoutMediator(it, pager, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = fragments[position].first
            }).attach()
        }
    }
}
