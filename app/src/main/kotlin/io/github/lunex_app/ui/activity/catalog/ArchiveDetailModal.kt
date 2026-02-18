package io.github.lunex_app.ui.activity.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.lunex_app.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ArchiveDetailModal(
    archiveId: Long,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    val vm = hiltViewModel<ArchiveDetailViewModel>()
    LaunchedEffect(archiveId) { vm.setArchive(archiveId) }
    val archive by vm.archive.collectAsStateWithLifecycle()

    ModalBottomSheet(onDismissRequest = onDismiss) {
        LI(
            title = "이름 바꾸기",
            icId = R.drawable.ic_edit,
            onClick = {
                onDismiss()
                onEdit()
            }
        )
        LI(
            title = "삭제",
            icId = R.drawable.ic_delete,
            onClick = {
                onDismiss()
                onDelete()
            }
        )
        HorizontalDivider()
        LI(
            title = "ID",
            content = archive?.data?.id?.toString() ?: ""
        )
        LI(
            title = "이름",
            content = archive?.data?.name ?: ""
        )
        LI(
            title = "항목 수",
            content = archive?.itemCount?.toString() ?: ""
        )
    }
}

@Composable
private fun LI(
    title: String,
    content: String? = null,
    icId: Int? = null,
    onClick: () -> Unit = {}
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = content?.let { { Text(it) } },
        leadingContent = {
            if (icId != null) Icon(painterResource(icId), null)
            else Box(modifier = Modifier.size(24.dp))
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier.clickable(onClick = onClick)
    )
}