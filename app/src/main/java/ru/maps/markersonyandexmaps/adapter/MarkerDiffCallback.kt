package ru.maps.markersonyandexmaps.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.maps.markersonyandexmaps.dto.Marker

class MarkerDiffCallback : DiffUtil.ItemCallback<Marker>() {

    override fun areItemsTheSame(oldItem: Marker, newItem: Marker) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Marker, newItem: Marker) = oldItem == newItem

}