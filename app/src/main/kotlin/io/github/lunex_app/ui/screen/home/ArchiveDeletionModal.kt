package io.github.lunex_app.ui.screen.home

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun ArchiveDeletionModal(archiveId: Long, onDismiss: () -> Unit) {
    val vm = hiltViewModel<ArchiveDeletionViewModel>()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("아카이브 삭제") },
        text = {
            Text("정말 삭제하시겠습니까?")
        },
        confirmButton = {
            Button(onClick = {
                vm.deleteArchive(archiveId)
                onDismiss()
            }) {
                Text("삭제")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}