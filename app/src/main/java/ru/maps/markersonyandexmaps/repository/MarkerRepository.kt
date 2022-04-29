package ru.maps.markersonyandexmaps.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import ru.maps.markersonyandexmaps.dto.Marker

interface MarkerRepository {
    val data: Flow<List<Marker>>
    suspend fun getMarker(id: Long): Marker
    suspend fun remove(id: Long)
    suspend fun save(marker: Marker): Long
}