package genos.synthetic.exported

import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_detail.*

inline val Fragment.exported_text1 get() = text1

// https://youtrack.jetbrains.com/issue/KT-22430
