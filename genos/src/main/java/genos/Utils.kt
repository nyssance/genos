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

package genos

import android.content.Context
import android.content.Intent
import android.widget.Toast
import genos.ui.activity.WebActivity

object Utils {
    @JvmStatic
    fun showLoginUI(context: Context, cls: Class<*>) {
        context.startActivity(Intent(context, cls))
    }

    @JvmStatic
    fun openLink(context: Context, url: String, title: String? = null) {
        if (url.isBlank()) {
            return
        }
        val intent = Intent(context, WebActivity::class.java)
        intent.putExtra("url", url)
        intent.putExtra("title", title)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}
