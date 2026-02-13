package io.github.lunex_app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [
        ArchiveEntity::class,
        ItemEntity::class,
        TagEntity::class,
        LinkEntity::class,
    ],
    exportSchema = false
)
abstract class LunexDatabase : RoomDatabase() {
    abstract fun lunexDao(): LunexDao
}
