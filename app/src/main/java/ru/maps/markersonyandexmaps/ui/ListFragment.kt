package ru.maps.markersonyandexmaps.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.AndroidEntryPoint
import ru.maps.markersonyandexmaps.R
import ru.maps.markersonyandexmaps.adapter.MarkerAdapter
import ru.maps.markersonyandexmaps.adapter.OnInteractionListener
import ru.maps.markersonyandexmaps.application.GlobalConstants
import ru.maps.markersonyandexmaps.databinding.FragmentListBinding
import ru.maps.markersonyandexmaps.dto.Marker
import ru.maps.markersonyandexmaps.view.SpacingItemDecorator
import ru.maps.markersonyandexmaps.viewModel.MarkerViewModel

@AndroidEntryPoint
class ListFragment : Fragment() {

    private val viewModel: MarkerViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentListBinding.inflate(layoutInflater, container, false)

        val adapter = MarkerAdapter(object : OnInteractionListener {
            override fun onEdit(marker: Marker) {
                viewModel.edit(marker)
                viewModel.save()
            }

            override fun onRemove(marker: Marker) {
                viewModel.remove(marker.id)
            }

            override fun onCameraPosition(latitude: Double, longitude: Double) {
                setFragmentResult(
                    GlobalConstants.KEY_CAMERA_POSITION,
                    bundleOf(
                        GlobalConstants.KEY_CAMERA_POSITION_LATITUDE to latitude,
                        GlobalConstants.KEY_CAMERA_POSITION_LONGITUDE to longitude
                    )
                )
                findNavController().navigate(R.id.action_listFragment_to_mapFragment)
            }
        })

        binding.markerList.adapter = adapter

        binding.markerList.addItemDecoration(
            SpacingItemDecorator(20)
        )

        viewModel.edited.observe(viewLifecycleOwner) { marker ->
            if (marker.id != 0L) {
                setFragmentResult(
                    GlobalConstants.KEY_POINT,
                    bundleOf(GlobalConstants.KEY_ID to marker.id)
                )
                findNavController().navigate(R.id.action_listFragment_to_editFragment)
            }
        }

        viewModel.data.observe(viewLifecycleOwner) { markers ->
            Log.i("myLocation", "$markers")
            val newMarker = adapter.itemCount < markers.size
            adapter.submitList(markers)
            if (newMarker) {
                binding.markerList.smoothScrollToPosition(0)
            }

        }

        return binding.root
    }
}