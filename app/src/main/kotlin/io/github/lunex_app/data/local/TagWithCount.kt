package io.github.lunex_app.data.local

import androidx.room.Embedded

data class TagWithCount(
    @Embedded val data: TagEntity,
    val itemCount: Int,
)
