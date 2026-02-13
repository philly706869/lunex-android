package io.github.lunex_app.data.local

import androidx.room.Embedded

data class ArchiveListItem(
    @Embedded val archive: ArchiveEntity,
    val itemCount: Int,
)
