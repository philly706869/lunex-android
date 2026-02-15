package io.github.lunex_app.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import io.github.lunex_app.data.model.QueryType
import io.github.lunex_app.data.model.TagQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface LunexDao {
    @Query(
        """
        SELECT a.*, COUNT(i.uuid) AS itemCount
        FROM archives a
        LEFT JOIN items i ON i.archive = a.id AND i.deletedAt IS NULL
        WHERE a.deletedAt IS NULL
        GROUP BY a.id
        ORDER BY a.name ASC
        """
    )
    fun getAllArchives(): PagingSource<Int, ArchiveWithCount>

    @Query(
        """
        SELECT a.*, COUNT(i.uuid) AS itemCount
        FROM archives a
        LEFT JOIN items i ON i.archive = a.id AND i.deletedAt IS NULL
        WHERE a.deletedAt IS NOT NULL
        GROUP BY a.id
        ORDER BY a.deletedAt DESC
        """
    )
    fun getDeletedArchives(): PagingSource<Int, ArchiveWithCount>

    @Query(
        """
        SELECT a.*, COUNT(i.uuid) AS itemCount
        FROM archives a
        LEFT JOIN items i ON i.archive = a.id AND i.deletedAt IS NULL
        WHERE a.id = :id AND a.deletedAt IS NULL
        GROUP BY a.id
        """
    )
    fun getArchiveWithCount(id: Long): Flow<ArchiveWithCount?>

    @Insert
    suspend fun insertArchive(archive: ArchiveEntity)

    @Query("UPDATE archives SET name = :name WHERE id = :id")
    suspend fun updateArchiveName(id: Long, name: String)

    @Query("UPDATE archives SET deletedAt = :timestamp WHERE id = :id")
    suspend fun deleteArchive(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE archives SET deletedAt = NULL WHERE id = :id")
    suspend fun restoreArchive(id: Long)

    @RawQuery(observedEntities = [ItemEntity::class, LinkEntity::class, TagEntity::class])
    fun getItemsByTags(query: SupportSQLiteQuery): PagingSource<Int, ItemEntity>

    @Query(
        """
        SELECT *
        FROM items
        WHERE archive = :archive AND deletedAt IS NOT NULL
        ORDER BY deletedAt DESC
        """
    )
    fun getDeletedItems(archive: Long): PagingSource<Int, ItemEntity>

    @Query(
        """
        SELECT i.*, t.name
        FROM items i
        LEFT JOIN links l ON l.uuid = i.uuid
        LEFT JOIN tags t ON t.archive = l.archive AND t.name = l.name AND t.deletedAt IS NULL
        WHERE i.uuid = :uuid
        """
    )
    fun getItem(uuid: String): Flow<Map<ItemEntity, List<String>>>

    @Insert
    suspend fun insertItem(item: ItemEntity)

    @Query("UPDATE items SET deletedAt = :timestamp WHERE uuid = :uuid")
    suspend fun deleteItem(uuid: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE items SET deletedAt = NULL WHERE uuid = :uuid")
    suspend fun restoreItem(uuid: String)

    @Query(
        """
        SELECT t.*
        FROM tags t
        WHERE t.archive = :archive AND t.deletedAt IS NULL
        ORDER BY t.name ASC
        """
    )
    fun getAllTags(archive: Long): Flow<List<TagWithCount>>

    @Query(
        """
        SELECT t.*, COUNT(i.uuid) as itemCount
        FROM tags t
        LEFT JOIN links l ON t.archive = l.archive AND t.name = l.name
        LEFT JOIN items i ON l.uuid = i.uuid AND i.deletedAt IS NULL
        WHERE t.archive = :archive AND t.deletedAt IS NULL
        GROUP BY t.name
        ORDER BY t.name ASC
        """
    )
    fun getAllTagsWithCount(archive: Long): PagingSource<Int, TagWithCount>

    @Query(
        """
        SELECT t.*, COUNT(i.uuid) as itemCount
        FROM tags t
        LEFT JOIN links l ON t.archive = l.archive AND t.name = l.name
        LEFT JOIN items i ON l.uuid = i.uuid AND i.deletedAt IS NULL
        WHERE t.archive = :archive AND t.deletedAt IS NOT NULL
        GROUP BY t.name
        ORDER BY t.deletedAt DESC
        """
    )
    fun getDeletedTagsWithCount(archive: Long): PagingSource<Int, TagWithCount>

    @Insert
    suspend fun insertTag(tag: TagEntity)

    @Query("UPDATE tags SET deletedAt = :timestamp WHERE archive = :archive AND name = :name")
    suspend fun deleteTag(archive: Long, name: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE tags SET deletedAt = NULL WHERE archive = :archive AND name = :name")
    suspend fun restoreTag(archive: Long, name: String)

    @Insert
    suspend fun insertLink(link: LinkEntity)

    @Update
    suspend fun updateLink(link: LinkEntity)

    @Query("DELETE FROM links WHERE archive = :archive AND uuid = :uuid AND name = :name")
    suspend fun deleteLink(archive: Long, uuid: String, name: String)

    companion object {
        fun buildTagSearchQuery(archive: Long, tagQueries: List<TagQuery>): SupportSQLiteQuery {
            val selectionParts = mutableListOf("archive = ?", "deletedAt IS NULL")
            val selectionArgs = mutableListOf<Any>(archive)

            tagQueries.forEach {
                val existsSql = when (it.type) {
                    QueryType.POSITIVE -> "EXISTS (SELECT 1 FROM links WHERE uuid = items.uuid AND name = ? AND flag = 1)"
                    QueryType.NEGATIVE -> "EXISTS (SELECT 1 FROM links WHERE uuid = items.uuid AND name = ? AND flag = 0)"
                    QueryType.UNDEFINED -> "NOT EXISTS (SELECT 1 FROM links WHERE uuid = items.uuid AND name = ?)"
                    QueryType.NOT_POSITIVE -> "NOT EXISTS (SELECT 1 FROM links WHERE uuid = items.uuid AND name = ? AND flag = 1)"
                    QueryType.NOT_NEGATIVE -> "NOT EXISTS (SELECT 1 FROM links WHERE uuid = items.uuid AND name = ? AND flag = 0)"
                    QueryType.DEFINED -> "EXISTS (SELECT 1 FROM links WHERE uuid = items.uuid AND name = ?)"
                }
                selectionParts.add(existsSql)
                selectionArgs.add(it.name)
            }

            val finalSelection = selectionParts.joinToString(" AND ")

            return SupportSQLiteQueryBuilder.builder("items")
                .columns(arrayOf("*"))
                .selection(finalSelection, selectionArgs.toTypedArray())
                .orderBy("createdAt DESC")
                .create()
        }
    }
}
