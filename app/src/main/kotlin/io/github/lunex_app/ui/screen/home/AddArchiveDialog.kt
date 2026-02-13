package io.github.lunex_app.ui.screen.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun AddArchiveDialog(
    onConfirm: (archiveName: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var archiveName by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("새 아카이브 생성") },
        text = {
            OutlinedTextField(
                label = { Text("아카이브 이름") },
                value = archiveName,
                onValueChange = { archiveName = it },
                singleLine = true,
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (archiveName.isNotBlank()) {
                        onConfirm(archiveName)
                        onDismiss()
                    }
                },
                enabled = archiveName.isNotBlank()
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