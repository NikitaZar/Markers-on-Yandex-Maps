package ru.maps.markersonyandexmaps.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.maps.markersonyandexmaps.dto.Marker

@Entity
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val description: String,
    val latitude: Double,
    val longitude: Double,
) {
    fun toDto() = Marker(id, description, latitude, longitude)

    companion object {
        fun fromDto(dto: Marker) =
            MarkerEntity(
                dto.id,
                dto.description,
                dto.latitude,
                dto.longitude
            )
    }
}

fun List<MarkerEntity>.toDto() = map { it.toDto() }

fun List<Marker>.toEntity() = map { MarkerEntity.fromDto(it) }