package io.github.lunex_app.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LunexDao {
    @Query(
        """
        SELECT a.*, COUNT(i.uuid) AS itemCount
        FROM archives a
        LEFT JOIN items i ON i.archive = a.id
        GROUP BY a.id
        ORDER BY a.name ASC
        """
    )
    fun getAllArchives(): PagingSource<Int, ArchiveListItem>

    @Query("SELECT * FROM archives WHERE id = :id")
    fun getArchive(id: Long): Flow<ArchiveEntity?>

    @Insert
    suspend fun insertArchive(archive: ArchiveEntity)

    @Update
    suspend fun updateArchive(archive: ArchiveEntity)

    @Delete
    suspend fun deleteArchive(archive: ArchiveEntity)
}
