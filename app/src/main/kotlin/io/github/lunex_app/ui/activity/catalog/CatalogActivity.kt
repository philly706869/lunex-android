package io.github.lunex_app.ui.activity.catalog

import android.os.Parcelable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import io.github.lunex_app.R
import io.github.lunex_app.ui.LunexActivity
import io.github.lunex_app.ui.activity.archive.ArchiveActivity
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
private sealed interface Modal : Parcelable {
    @Parcelize
    data object Add : Modal

    @Parcelize
    data class Detail(val archiveId: Long) : Modal

    @Parcelize
    data class Edit(val archiveId: Long) : Modal

    @Parcelize
    data class Delete(val archiveId: Long) : Modal
}

@AndroidEntryPoint
class CatalogActivity : LunexActivity() {
    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    override fun Content() {
        val vm = hiltViewModel<CatalogViewModel>()
        val archives = vm.archives.collectAsLazyPagingItems()
        var modal by rememberSaveable { mutableStateOf<Modal?>(null) }
        val archiveActivityLauncher =
            rememberLauncherForActivityResult(ArchiveActivity.Contract()) {}

        when (val capturedModal = modal) {
            null -> {}

            is Modal.Add -> ArchiveCreationModal(
                onDismiss = { modal = null }
            )

            is Modal.Detail -> ArchiveDetailModal(
                archiveId = capturedModal.archiveId,
                onEdit = { modal = Modal.Edit(capturedModal.archiveId) },
                onDelete = { modal = Modal.Delete(capturedModal.archiveId) },
                onDismiss = { modal = null }
            )

            is Modal.Edit -> ArchiveEditModal(
                archiveId = capturedModal.archiveId,
                onDismiss = { modal = null }
            )

            is Modal.Delete -> ArchiveDeletionModal(
                archiveId = capturedModal.archiveId,
                onDismiss = { modal = null }
            )
        }

        Scaffold(
            topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
            floatingActionButton = {
                FloatingActionButton(onClick = { modal = Modal.Add }) {
                    Icon(painterResource(R.drawable.ic_add), null)
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(128.dp),
                    contentPadding = PaddingValues(8.dp),
                ) {
                    items(
                        count = archives.itemCount,
                        key = archives.itemKey { it.data.id },
                    ) { index ->
                        val archive = archives[index]
                        Box(
                            modifier = Modifier
                                .combinedClickable(
                                    onClick = {
                                        archive?.let {
                                            archiveActivityLauncher.launch(
                                                ArchiveActivity.Input(it.data.id)
                                            )
                                        }
                                    },
                                    onLongClick = {
                                        archive?.let {
                                            modal = Modal.Detail(it.data.id)
                                        }
                                    }
                                )
                                .padding(8.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                GlideImage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .border(
                                            width = 0.5.dp,
                                            color = MaterialTheme.colorScheme.outlineVariant,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(0.5.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    imageModel = { File("/storage/0000-0000/Download/profile.png") }
                                )
                                Column {
                                    Text(
                                        archive?.data?.name ?: "",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        archive?.itemCount?.toString() ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
