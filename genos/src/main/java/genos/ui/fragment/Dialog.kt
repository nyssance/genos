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

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

// Android https://developer.android.com/guide/topics/ui/dialogs

class Dialog(
        private val title: String,
        private val message: String?,
        private val action: ((DialogInterface, Int) -> Unit)?
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok) { dialog, which ->
                        action?.let {
                            it(dialog, which)
                        }
                    }
            action?.let {
                builder.setNegativeButton(android.R.string.cancel, null)
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
