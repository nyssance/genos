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

package genos.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import genos.R
import genos.ui.activity.base.NavigationActivity

abstract class TabBarActivity : NavigationActivity() {
    protected var shiftingMode: Boolean = false

    override fun onSetContentView(name: String) {
        setContentView(R.layout.activity_tab_bar)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener { item -> onNavigationItemSelected(item.itemId) }

        // SO: https://stackoverflow.com/questions/40396545/bottomnavigationview-display-both-icons-and-text-labels-at-all-times
        val menu = navigation.getChildAt(0) as BottomNavigationMenuView
        val count = menu.childCount
        if (count > 3) {
            //            setField(menu.getClass(), menu, "mShiftingMode", mShiftingMode);
            for (i in 0 until count) {
                val item = menu.getChildAt(i) as BottomNavigationItemView
                //                item.setShiftingMode(mShiftingMode);
                item.setShifting(true)
                item.setChecked(true)
            }
        }
    }

    private fun setField(targetClass: Class<Any>, instance: Any, fieldName: String, value: Any) {
        try {
            val field = targetClass.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(instance, value)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

    }
}
