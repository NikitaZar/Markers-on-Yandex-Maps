package ru.maps.markersonyandexmaps.model

import ru.maps.markersonyandexmaps.dto.Marker

data class FeedModel(
    val markers: List<Marker> = emptyList(),
)