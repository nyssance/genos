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

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;
import genos.Helper;
import genos.R;
import genos.libs.MessageEvent;

public abstract class BaseActivity extends AppCompatActivity {
    protected CollapsingToolbarLayout mCollapsingToolbar;
    protected Toolbar mNavigationBar;
    protected Toolbar mToolbar;
    private int mMenuRes;
    private OnBackPressedListener mOnBackPressedListener;
    private OnKeyUpListener mOnKeyUpListener;

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    public void setOnKeyUpListener(OnKeyUpListener listener) {
        mOnKeyUpListener = listener;
    }

    public Toolbar getNavigationBar() {
        return mNavigationBar;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String className = getClass().getSimpleName();
        Logger.t("base").i(className + " : onCreate()");
        String name = className.replace("Activity", "")
                .replaceAll("(.)(\\p{Upper})", "$1_$2")
                .toLowerCase(Locale.ENGLISH);
        onSetContentView(name);
        mMenuRes = Helper.getResId(this, name, "menu");
        // 可折叠顶栏
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);
        // 顶部导航栏
        mNavigationBar = findViewById(R.id.navigation_bar);
        if (mNavigationBar != null) {
            setSupportActionBar(mNavigationBar);
        }
        // 底部工具栏
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            int resId = Helper.getResId(this, name + "_toolbar", "menu");
            if (resId > 0) {
                mToolbar.inflateMenu(resId);
            }
        }
        // 默认显示 Up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (getTitle().equals(Helper.getApplicationName(this))) { // Activity android:label not set
                int resId = Helper.getResId(this, name, "string");
                if (resId > 0) {
                    actionBar.setTitle(resId);
                }
            }
        }
    }

    protected void onSetContentView(@NonNull String name) {
        String layoutName = "activity_" + name;
        int layoutResID = Helper.getResId(this, layoutName, "layout");
        if (layoutResID > 0) { // NY: 如果子类重新setContentView并且和此处同名，同时包含fragment的话，会报错
            setContentView(layoutResID);
        } else {
            Logger.t("base").wtf(layoutName + ".xml not exists!");
        }
    }

    @Override
    protected final void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mMenuRes > 0) {
            getMenuInflater().inflate(mMenuRes, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected final void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Wrong doc: https://developer.android.com/training/implementing-navigation/ancestral.html#up
        // SO: https://stackoverflow.com/a/31350642
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent) || isTaskRoot()) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else { // NY: NavUtils依然会重载入父页面, 所以增加FLAG_ACTIVITY_CLEAR_TOP
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null) {
            if (!mOnBackPressedListener.onBackPressed()) {
                super.onBackPressed();
            }
        } else { // 如果为null, 执行系统默认
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.isTracking() && !event.isCanceled()) {
            if (mOnKeyUpListener != null) {
                return mOnKeyUpListener.onKeyUp(keyCode, event) || super.onKeyUp(keyCode, event);
            } else { // 如果为null, 执行系统默认
                return super.onKeyUp(keyCode, event);
            }
        } else {
            return false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(MessageEvent event) {
        Logger.t("EventBus").w("Activity: " + getClass().getSimpleName() + " 收到了 Fragment: " + event.sender.getClass().getSimpleName() + " 的消息: "
                + event.message);
    }

    public interface OnBackPressedListener {
        boolean onBackPressed();
    }

    public interface OnKeyUpListener {
        boolean onKeyUp(int keyCode, KeyEvent event);
    }
}
