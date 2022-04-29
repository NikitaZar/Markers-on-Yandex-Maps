package ru.maps.markersonyandexmaps.adapter

import ru.maps.markersonyandexmaps.dto.Marker

interface OnInteractionListener {
    fun onEdit(marker: Marker)
    fun onRemove(marker: Marker)
}