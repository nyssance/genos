package com.example.genos

import androidx.compose.Composable
import androidx.ui.material.MaterialTheme
import androidx.ui.tooling.preview.Preview
import com.example.genos.ui.Discover
import com.example.genos.ui.UserDetail

@Preview
@Composable
private fun 发现() {
    MaterialTheme {
        Discover("Android")
    }
}

@Preview
@Composable
private fun 用户详情() {
    MaterialTheme {
        UserDetail("detail")
    }
}
