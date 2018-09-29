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

package genos.ui.activity.base;

import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import android.util.SparseArray;

import genos.R;

public abstract class NavigationActivity extends BaseActivity {
    protected SparseArray<Fragment> mFragments = new SparseArray<>();
    private Fragment mCurrentFragment;
    private String mCurrentTag = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) { // NY: 导航页一般做为首页, 不显示Up button
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    protected boolean onNavigationItemSelected(@IdRes int id) {
        String tag = String.format("%s", id);
        if (mCurrentTag.equals(tag)) {
            return false;
        }
        mCurrentTag = tag;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(mCurrentTag); // 目标Fragment
        if (fragment == null) {
            fragment = mFragments.get(id);
            transaction.add(R.id.container_for_add, fragment, mCurrentTag);
        } else {
            transaction.show(fragment);
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
        mCurrentFragment = fragment;
        return true;
    }
}
