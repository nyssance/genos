package com.example.genos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.Composable
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.layout.Padding
import androidx.ui.layout.Table
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import com.example.genos.model.User
import com.nyssance.genos.R
import genos.ui.fragment.generic.Detail

class Discover : Detail<User>() {
    override fun onCreate() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        container?.setContent {
            Greeting("Android")
        }
        return view
    }

    override fun onDisplay(data: User) {}
}

@Composable
fun Greeting(name: String) {
    Text("Hello $name!")
    Padding(2.dp) {
        Table(columns = 8) {
            for (i in 0 until 8) {
                tableRow {
                    for (j in 0 until 8) {
                        Padding(2.dp) {
                            Text("TX")
                        }
                    }
                }
            }
        }
    }
    Text("Hello $name!")
}

@Preview
@Composable
fun PreviewGreeting() {
    MaterialTheme() {
        Greeting("Android")
        Text(text = "Hello!")
    }
}
