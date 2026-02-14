package io.github.lunex_app.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "tags",
    primaryKeys = ["archive", "name"],
    foreignKeys = [
        ForeignKey(
            entity = ArchiveEntity::class,
            parentColumns = ["id"],
            childColumns = ["archive"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index("deletedAt", "createdAt"),
    ],
)
data class TagEntity(
    val archive: Long,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null,
)
