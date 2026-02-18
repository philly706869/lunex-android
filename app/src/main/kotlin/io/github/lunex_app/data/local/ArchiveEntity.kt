package io.github.lunex_app.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "archives",
    indices = [
        Index("deletedAt", "name"),
    ],
)
data class ArchiveEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val path: String,
    val createdAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)
