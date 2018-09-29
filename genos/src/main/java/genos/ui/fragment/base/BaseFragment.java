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

package genos.ui.fragment.base;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import genos.R;
import genos.ui.activity.base.BaseActivity;

public abstract class BaseFragment extends Fragment implements BaseActivity.OnBackPressedListener, BaseActivity.OnKeyUpListener {
    // 💖 Lifecycle
    // https://developer.android.com/guide/components/fragments.html#Lifecycle

    /**
     * onAttach() : 绑定Activity的callback
     */

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onPrepare();
    }

    /**
     * onPrepare() : 初始化, mCall, mTileId, setHasOptionsMenu
     */
    protected abstract void onPrepare();

    /**
     * onCreateView() : 布局, Fragment会被混淆, 所以都需要手动设置
     */

    /**
     * onViewCreated() : ensureList()，各种findViewById获取View
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = view.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(v -> Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_SHORT).show());
        }
    }

    /**
     * onActivityCreated() : 设置Adapter，List选中样式等在这里添加，加载数据前的最终初始化
     */

    /**
     * onPause() : 需要持久化纪录状态的写在这里, 因为用户可能不返回了
     */

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        return onPerform(item.getItemId());
    }

    @Override
    public final boolean onKeyUp(int keyCode, KeyEvent event) {
        Logger.t("base").i("onKeyUp keyCode: " + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_SEARCH: // 处理一些设备的专用search按钮
                return true;
            case KeyEvent.KEYCODE_F1:
                return onPerform(R.id.action_help);
            case KeyEvent.KEYCODE_F5:
                return onPerform(R.id.action_view_refresh);
            case KeyEvent.KEYCODE_ENTER:
                return true;
            case KeyEvent.KEYCODE_ESCAPE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    protected abstract boolean onPerform(int action);

    /**
     * 工具方法
     */
    public final void startActivitySafely(Intent intent) {
        startActivitySafely(intent, false); // 默认为不开启外部Activity
    }

    public final void startActivitySafely(Intent intent, boolean newTask) {
        if (newTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 该句决定是否在同一进程中
        }
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException | SecurityException e) {
            postError(e.getLocalizedMessage());
        }
    }

    public final void setTitle(CharSequence title) {
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        } else {
            Logger.t("base").w("No action bar!");
        }
    }

    public final void postError(CharSequence text) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show();
    }
}
