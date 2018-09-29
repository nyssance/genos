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

package genos.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;

import genos.R;
import genos.ui.activity.base.NavigationActivity;

public abstract class TabBarActivity extends NavigationActivity {
    protected boolean mShiftingMode;

    @Override
    protected void onSetContentView(String name) {
        setContentView(R.layout.activity_tab_bar);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(item -> onNavigationItemSelected(item.getItemId()));

        // SO: https://stackoverflow.com/questions/40396545/bottomnavigationview-display-both-icons-and-text-labels-at-all-times
        BottomNavigationMenuView menu = (BottomNavigationMenuView) navigation.getChildAt(0);
        int count = menu.getChildCount();
        if (count > 3) {
//            setField(menu.getClass(), menu, "mShiftingMode", mShiftingMode);
            for (int i = 0; i < count; i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menu.getChildAt(i);
//                item.setShiftingMode(mShiftingMode);
                item.setShifting(true);
                item.setChecked(true);
            }
        }
    }

    private void setField(Class targetClass, Object instance, String fieldName, Object value) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
