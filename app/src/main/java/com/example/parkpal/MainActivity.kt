package com.example.parkpal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.parkpal.ui.theme.ParkpalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            ParkpalTheme(darkTheme = isDarkTheme) {
                AppNavHost(
                    isDarkTheme = isDarkTheme,
                    onDarkThemeToggle = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}