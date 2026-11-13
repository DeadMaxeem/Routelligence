package com.routelligence.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.routelligence.app.model.UserProfile
import com.routelligence.app.ui.HomeScreen
import com.routelligence.app.ui.PreferencesScreen
import com.routelligence.app.ui.RoutePlannerScreen
import com.routelligence.app.ui.theme.RoutelligenceTheme

private enum class AppTab(val label: String) {
    HOME("Start"), ROUTE("Trasa"), SETTINGS("Ustawienia")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoutelligenceTheme {
                var selectedTab by rememberSaveable { mutableStateOf(AppTab.HOME.name) }
                var profile by remember { mutableStateOf(UserProfile()) }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            AppTab.entries.forEach { tab ->
                                NavigationBarItem(
                                    selected = selectedTab == tab.name,
                                    onClick = { selectedTab = tab.name },
                                    icon = { Text(tab.label.take(1)) },
                                    label = { Text(tab.label) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    when (AppTab.valueOf(selectedTab)) {
                        AppTab.HOME -> HomeScreen(profile = profile)
                        AppTab.ROUTE -> RoutePlannerScreen(profile = profile)
                        AppTab.SETTINGS -> PreferencesScreen(
                            profile = profile,
                            onProfileChange = { profile = it }
                        )
                    }
                }
            }
        }
    }
}
