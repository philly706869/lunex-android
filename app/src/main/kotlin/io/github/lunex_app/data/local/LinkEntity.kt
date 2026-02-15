package io.github.lunex_app.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "links",
    primaryKeys = ["uuid", "archive", "name"],
    foreignKeys = [
        ForeignKey(
            entity = ArchiveEntity::class,
            parentColumns = ["id"],
            childColumns = ["archive"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ItemEntity::class,
            parentColumns = ["uuid", "archive"],
            childColumns = ["uuid", "archive"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["archive", "name"],
            childColumns = ["archive", "name"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [
        Index("archive", "name", "flag", "uuid")
    ],
)
data class LinkEntity(
    val uuid: String,
    val archive: Long,
    val name: String,
    val flag: Boolean,
)
