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

package genos.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebView
import androidx.core.content.ContextCompat
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.nyssance.genos.R
import genos.ui.activity.base.BaseActivity

class WebActivity : BaseActivity(R.layout.activity_app_bar) {
    private var url = ""
    private val webViewClient = object : WebViewClient() {}
    private val webChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            if (this@WebActivity.title.isEmpty()) {
                supportActionBar?.title = title
            }
        }
    }
    private val agentWeb by lazy {
        AgentWeb.with(this)
            .setAgentWebParent(findViewById(R.id.container_for_add), ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))
            .useDefaultIndicator(ContextCompat.getColor(this, R.color.colorPrimaryVariant))
//            .setWebView(getWebView())
            .setWebChromeClient(webChromeClient)
            .setWebViewClient(webViewClient)
            .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK) // 打开其他应用时，弹窗咨询用户是否前往其他应
//            .setPermissionInterceptor(getPermissionInterceptor())
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .interceptUnkownUrl() // 拦截找不到相关页面的Scheme
            .createAgentWeb()
            .ready()
            .go(url)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(intent) {
            url = data.toString()
            getStringExtra("title")?.let {
                title = it
            }
        }
//        agentWeb.urlLoader.loadUrl(url)
    }

    override fun onResume() {
        agentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onPause() {
        agentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        agentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?) = if (agentWeb.handleKeyEvent(keyCode, event)) true else super.onKeyDown(keyCode, event)

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_open_in_browser -> {
            // <a href="intent://stackoverflow.com/questions/29250152/what-is-the-intent-to-launch-any-website-link-in-google-chrome#Intent;scheme=http;package=com.android.chrome;end">
            val href = url.split("://")
            startActivity(Intent.parseUri("intent://${href.last()}#Intent;scheme=${href.first()};action=android.intent.action.VIEW;end;", Intent.URI_INTENT_SCHEME))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
