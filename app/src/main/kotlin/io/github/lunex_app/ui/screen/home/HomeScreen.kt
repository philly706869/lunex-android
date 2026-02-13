package io.github.lunex_app.ui.screen.home

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import io.github.lunex_app.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(vm: HomeViewModel = hiltViewModel()) {
    val archives = vm.archives.collectAsLazyPagingItems()
    var showAddArchiveDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddArchiveDialog = true }) {
                Icon(painterResource(R.drawable.ic_add), null)
            }
        }
    ) { padding ->
        if (showAddArchiveDialog) {
            AddArchiveDialog(
                onConfirm = { archiveName -> vm.createArchive(archiveName) },
                onDismiss = { showAddArchiveDialog = false }
            )
        }

        Box(modifier = Modifier.padding(padding)) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(128.dp),
                contentPadding = PaddingValues(8.dp),
            ) {
                items(
                    count = archives.itemCount,
                    key = archives.itemKey { it.archive.id },
                ) { index ->
                    Box(
                        modifier = Modifier
                            .combinedClickable(
                                onClick = {},
                                onLongClick = {}
                            )
                            .padding(8.dp)
                    ) {
                        val item = archives[index]
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            AsyncImage(
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
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://picsum.photos/500/500")
                                    .memoryCachePolicy(CachePolicy.DISABLED)
                                    .diskCachePolicy(CachePolicy.DISABLED)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    item?.archive?.name ?: "",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    item?.itemCount?.toString() ?: "",
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

