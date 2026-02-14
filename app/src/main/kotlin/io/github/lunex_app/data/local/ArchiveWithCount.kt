package io.github.lunex_app.data.local

import androidx.room.Embedded

data class ArchiveWithCount(
    @Embedded val data: ArchiveEntity,
    val itemCount: Int,
)
