package io.github.lunex_app.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LunexDao {
    @Query(
        """
        SELECT a.*, (SELECT COUNT(*) FROM items WHERE archive = a.id) AS itemCount
        FROM archives a
        WHERE a.deletedAt IS NULL
        ORDER BY a.name ASC
        """
    )
    fun getAllArchives(): PagingSource<Int, ArchiveWithCount>

    @Query(
        """
        SELECT a.*, (SELECT COUNT(*) FROM items WHERE archive = a.id) AS itemCount
        FROM archives a
        WHERE a.id = :id AND a.deletedAt IS NULL
        """
    )
    fun getArchive(id: Long): Flow<ArchiveWithCount?>

    @Insert
    suspend fun insertArchive(archive: ArchiveEntity)

    @Query("UPDATE archives SET name = :name WHERE id = :id")
    suspend fun updateArchiveName(id: Long, name: String)

    @Query(
        """
        SELECT a.*, (SELECT COUNT(*) FROM items WHERE archive = a.id) AS itemCount
        FROM archives a
        WHERE a.deletedAt IS NOT NULL
        ORDER BY a.name ASC
        """
    )
    fun getAllDeletedArchives(): PagingSource<Int, ArchiveWithCount>

    @Query("UPDATE archives SET deletedAt = :timestamp WHERE id = :id")
    suspend fun deleteArchive(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE archives SET deletedAt = NULL WHERE id = :id")
    suspend fun restoreArchive(id: Long)

    @Insert
    suspend fun insertItem(item: ItemEntity)

    @Query("UPDATE items SET deletedAt = :timestamp WHERE archive = :archive AND uuid = :uuid")
    suspend fun deleteItem(
        archive: Long,
        uuid: String,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("UPDATE items SET deletedAt = NULL WHERE archive = :archive AND uuid = :uuid")
    suspend fun restoreItem(archive: Long, uuid: String)

    @Query(
        """
        SELECT t.*, COUNT(l.uuid) as itemCount
        FROM tags t
        LEFT JOIN links l ON t.archive = l.archive AND t.name = l.name
        WHERE t.archive = :archive
        GROUP BY t.name
        ORDER BY t.name ASC
        """
    )
    fun getAllTags(archive: Long): Flow<List<TagWithCount>>

    @Insert
    suspend fun insertTag(tag: TagEntity)


    @Insert
    suspend fun insertLink(link: LinkEntity)

    @Delete
    suspend fun deleteLink(link: LinkEntity)
}
