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

package genos.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.nyssance.genos.R
import kotlinx.android.synthetic.main.fragment_placeholder.*

class PlaceholderFragment : Fragment(R.layout.fragment_placeholder) {
    companion object {
        const val ARG_SECTION_TEXT = "section_text"

        fun instance(text: String = "") = PlaceholderFragment().apply {
            val args = Bundle()
            args.putString(ARG_SECTION_TEXT, text)
            arguments = args
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.apply {
            text1.text = getString(ARG_SECTION_TEXT)
        }
    }
}
