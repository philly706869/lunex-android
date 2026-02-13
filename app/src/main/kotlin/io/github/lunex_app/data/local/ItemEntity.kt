package io.github.lunex_app.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "items",
    primaryKeys = ["archive", "uuid"],
    foreignKeys = [
        ForeignKey(
            entity = ArchiveEntity::class,
            parentColumns = ["id"],
            childColumns = ["archive"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.RESTRICT,
        )
    ],
    indices = [
        Index("createdAt"),
        Index("indexedAt"),
        Index("deletedAt"),
    ],
)
data class ItemEntity(
    val archive: Long,
    val uuid: String,
    val createdAt: Long,
    val indexedAt: Long,
    val deletedAt: Long?,
)
