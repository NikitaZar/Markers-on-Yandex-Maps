package ru.maps.markersonyandexmaps.adapter

import com.yandex.mapkit.geometry.Point
import ru.maps.markersonyandexmaps.dto.Marker

interface OnInteractionListener {
    fun onEdit(marker: Marker)
    fun onRemove(marker: Marker)
    fun onCameraPosition(latitude: Double, longitude: Double)
}