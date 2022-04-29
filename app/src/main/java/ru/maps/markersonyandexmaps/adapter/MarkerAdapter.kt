package ru.maps.markersonyandexmaps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.maps.markersonyandexmaps.databinding.CardMarkerBinding
import ru.maps.markersonyandexmaps.dto.Marker

class MarkerAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Marker, MarkerViewHolder>(MarkerDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val binding = CardMarkerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarkerViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        val marker = getItem(position)
        holder.bind(marker)
    }
}