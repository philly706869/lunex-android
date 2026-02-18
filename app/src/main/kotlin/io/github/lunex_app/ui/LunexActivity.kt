package io.github.lunex_app.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect

abstract class LunexActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var enabled by remember { mutableStateOf(Environment.isExternalStorageManager()) }
            LifecycleResumeEffect(Unit) {
                enabled = Environment.isExternalStorageManager()
                onPauseOrDispose {}
            }

            MaterialTheme(
                colorScheme =
                    if (isSystemInDarkTheme()) dynamicDarkColorScheme(this)
                    else dynamicLightColorScheme(this)
            ) {
                if (enabled) Content()
                else ManageExternalStoragePermissionRequestScreen {
                    enabled = Environment.isExternalStorageManager()
                }
            }
        }
    }

    @Composable
    abstract fun Content()

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun ManageExternalStoragePermissionRequestScreen(onResult: () -> Unit) {
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { onResult() }

        Scaffold(topBar = { TopAppBar(title = { Text("Lunex 필수 권한 요청") }) }) { padding ->
            Box(
                modifier = Modifier.Companion
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Companion.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Companion.CenterHorizontally
                ) {
                    Text("Lunex는 모든 파일에 접근할 권한이 필요합니다")
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(onClick = { finishAffinity() }) { Text("거절") }
                        Button(onClick = {
                            launcher.launch(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                                data = Uri.fromParts("package", packageName, null)
                            })
                        }) {
                            Text("허용")
                        }
                    }
                }
            }
        }
    }
}