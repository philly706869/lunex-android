package io.github.lunex_app.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "items",
    primaryKeys = ["uuid"],
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
        Index("archive", "uuid", unique = true),
        Index("archive", "deletedAt", "createdAt"),
        Index("archive", "deletedAt", "indexedAt"),
    ],
)
data class ItemEntity(
    val uuid: String,
    val archive: Long,
    val createdAt: Long,
    val indexedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null,
)
