package ru.maps.markersonyandexmaps.adapter

import androidx.recyclerview.widget.RecyclerView
import com.yandex.mapkit.geometry.Point
import ru.maps.markersonyandexmaps.databinding.CardMarkerBinding
import ru.maps.markersonyandexmaps.dto.Marker

class MarkerViewHolder(
    private val binding: CardMarkerBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(marker: Marker) {
        binding.apply {
            description.text = marker.description
            latitude.editText?.setText(String.format("%.7f", marker.latitude))
            longitude.editText?.setText(String.format("%.7f", marker.longitude))

            btDelete.setOnClickListener {
                onInteractionListener.onRemove(marker)
            }

            btEdit.setOnClickListener {
                onInteractionListener.onEdit(marker)
            }

            btNavigate.setOnClickListener {
                onInteractionListener.onCameraPosition(marker.latitude, marker.longitude)
            }
        }
    }
}