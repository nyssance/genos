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

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

object Global {
    enum class AuthPrefix {
        Bearer, JWT
    }

    var APP_SCHEME = "genos"
    var AUTH_HEADER = "Authorization"
    var AUTH_PREFIX = AuthPrefix.Bearer.name
    var AUTH_TOKEN = ""
    var LIST_START_PAGE = 1

    init {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(2)
                .tag("Genos")
                .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
//        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
//            override fun isLoggable(priority: Int, tag: String?) = BuildConfig.DEBUG
//        })
    }
}
