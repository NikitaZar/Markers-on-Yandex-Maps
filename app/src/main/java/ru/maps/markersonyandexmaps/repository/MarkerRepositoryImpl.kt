package ru.maps.markersonyandexmaps.repository

import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import ru.maps.markersonyandexmaps.dao.MarkerDao
import ru.maps.markersonyandexmaps.dto.Marker
import ru.maps.markersonyandexmaps.entity.MarkerEntity
import ru.maps.markersonyandexmaps.entity.toDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarkerRepositoryImpl @Inject constructor(
    private val dao: MarkerDao
) : MarkerRepository {

    override val data = dao.getAll()
        .map(List<MarkerEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override suspend fun getMarker(id: Long) = dao.getMarker(id).toDto()

    override suspend fun remove(id: Long) = dao.remove(id)

    override suspend fun save(marker: Marker) = dao.insert(MarkerEntity.fromDto(marker))
}