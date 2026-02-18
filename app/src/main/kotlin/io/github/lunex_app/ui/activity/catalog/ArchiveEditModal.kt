package io.github.lunex_app.ui.activity.catalog

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun ArchiveEditModal(archiveId: Long, onDismiss: () -> Unit) {
    val vm = hiltViewModel<ArchiveEditViewModel>()
    var name by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("아카이브 이름 바꾸기") },
        text = {
            OutlinedTextField(
                label = { Text("아카이브 이름") },
                value = name,
                onValueChange = { name = it },
                singleLine = true,
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        vm.updateArchive(archiveId, name)
                        onDismiss()
                    }
                },
                enabled = name.isNotBlank()
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