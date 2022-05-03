package ru.maps.markersonyandexmaps.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.maps.markersonyandexmaps.entity.MarkerEntity

@Dao
interface MarkerDao {
    @Query("SELECT * FROM MarkerEntity ORDER BY id DESC")
    fun getAll(): Flow<List<MarkerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: MarkerEntity): Long

    @Query("DELETE FROM MarkerEntity WHERE id =:id")
    suspend fun remove(id: Long)

    @Query("SELECT * FROM MarkerEntity WHERE id = :id")
    suspend fun getMarker(id: Long): MarkerEntity
}