package com.example.genos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.genos.model.User
import com.orhanobut.logger.Logger
import genos.extension.showActionSheet
import genos.extension.showAlert
import genos.model.Item
import genos.ui.fragment.generic.Detail
import org.jetbrains.anko.button
import org.jetbrains.anko.editText
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.verticalLayout

class Discover : Detail<User>() {
    override fun onCreate() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI {
            verticalLayout {
                val name = editText()
                button("Say Hello") {
                    setOnClickListener { toast("Hello, ${name.text}!") }
                }
                button("ActionSheet") {
                    setOnClickListener {
                        val items = arrayListOf(
                                Item(requireContext(), android.R.string.copy, getString(android.R.string.copy)),
                                Item(requireContext(), android.R.string.paste, getString(android.R.string.paste))
                        )
                        showActionSheet("title", items) {
                            showAlert(it.name) { _, which ->
                                Logger.wtf("$which")
                            }
                        }
                    }
                }
            }
        }.view
    }

    override fun onDisplay(data: User) {}
}
