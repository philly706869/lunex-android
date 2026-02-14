package io.github.lunex_app.ui.screen.archive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ArchiveScreen(archiveId: Long) {
    val vm = hiltViewModel<ArchiveViewModel>()
    LaunchedEffect(archiveId) { vm.setArchive(archiveId) }
    val archive by vm.archive.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Column {
                    Text(
                        archive?.data?.name ?: "",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        archive?.itemCount?.toString() ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            })
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {

        }
    }
}