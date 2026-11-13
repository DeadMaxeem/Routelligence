package com.routelligence.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.routelligence.app.ui.HomeScreen
import com.routelligence.app.ui.theme.RoutelligenceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoutelligenceTheme {
                HomeScreen()
            }
        }
    }
}
