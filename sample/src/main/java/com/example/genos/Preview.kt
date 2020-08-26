@file:Suppress("NonAsciiCharacters")

package com.example.genos

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.ui.tooling.preview.Preview
import com.example.genos.ui.Discover
import com.example.genos.ui.UserDetail

@Preview
@Composable
private fun 发现() {
    MaterialTheme {
        Discover("discover")
    }
}

@Preview
@Composable
private fun 用户详情() {
    MaterialTheme {
        UserDetail("user")
    }
}
