package io.github.lunex_app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import io.github.lunex_app.ui.navigation.Screen
import io.github.lunex_app.ui.screen.home.HomeScreen

@AndroidEntryPoint
class LunexActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }

            LunexTheme {
                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = entryProvider<Screen> {
                        entry<Screen.Home> { HomeScreen() }
                    }
                )
            }
        }
    }
}
