package ru.maps.markersonyandexmaps.repository

import kotlinx.coroutines.flow.Flow
import ru.maps.markersonyandexmaps.dto.Marker

interface MarkerRepository {
    val data: Flow<List<Marker>>
    suspend fun getMarker(id: Long)
    suspend fun remove(id: Long)
    suspend fun save(marker: Marker): Long
}