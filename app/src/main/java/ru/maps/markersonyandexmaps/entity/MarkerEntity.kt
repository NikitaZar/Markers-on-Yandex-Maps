package ru.maps.markersonyandexmaps.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.maps.markersonyandexmaps.dto.Marker

@Entity
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val description: String
) {
    fun toDto() = Marker(id, description)

    companion object {
        fun fromDto(dto: Marker) =
            MarkerEntity(
                dto.id,
                dto.description
            )
    }
}

fun List<MarkerEntity>.toDto() = map { it.toDto() }

fun List<Marker>.toEntity() = map { MarkerEntity.fromDto(it) }