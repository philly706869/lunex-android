package io.github.lunex_app.ui.activity.catalog

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import io.github.lunex_app.ui.activity.explorer.ExplorerActivity

@Composable
fun ArchiveCreationModal(onDismiss: () -> Unit) {
    val vm = hiltViewModel<ArchiveCreationViewModel>()
    var name by rememberSaveable { mutableStateOf("") }
    var path by rememberSaveable { mutableStateOf<String?>(null) }
    val interactionSource = remember { MutableInteractionSource() }
    val explorerActivityLauncher =
        rememberLauncherForActivityResult(ExplorerActivity.Contract()) {
            it?.let {
                path = it.path
            }
        }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect {
            if (it is PressInteraction.Release) {
                explorerActivityLauncher.launch(Unit)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("새 아카이브 생성") },
        text = {
            Column {
                OutlinedTextField(
                    label = { Text("아카이브 이름") },
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                )
                OutlinedTextField(
                    label = { Text("저장 경로") },
                    value = path ?: "",
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    interactionSource = interactionSource,
                )
            }
        },
        confirmButton = {
            val path = path
            Button(
                enabled = name.isNotBlank() && path != null,
                onClick = {
                    if (name.isNotBlank() && path != null) {
                        vm.createArchive(name, path)
                        onDismiss()
                    }
                },
            ) {
                Text("생성")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}