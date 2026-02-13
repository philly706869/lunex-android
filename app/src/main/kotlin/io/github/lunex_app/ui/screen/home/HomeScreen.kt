package io.github.lunex_app.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.lunex_app.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen() {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.app_name)) }
        )
    }) { padding ->
        Box(modifier = Modifier.padding(padding)) {

        }
    }
}
