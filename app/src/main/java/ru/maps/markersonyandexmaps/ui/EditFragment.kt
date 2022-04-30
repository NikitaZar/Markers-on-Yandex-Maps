package ru.maps.markersonyandexmaps.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.maps.markersonyandexmaps.application.GlobalConstants
import ru.maps.markersonyandexmaps.databinding.FragmentEditBinding
import ru.maps.markersonyandexmaps.dto.Marker
import ru.maps.markersonyandexmaps.viewModel.MarkerViewModel
import kotlin.properties.Delegates

@AndroidEntryPoint
class EditFragment : Fragment() {

    private var latitude by Delegates.notNull<Double>()
    private var longitude by Delegates.notNull<Double>()
    private var id by Delegates.notNull<Long>()
    private val viewModel: MarkerViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditBinding.inflate(layoutInflater)

        setFragmentResultListener(GlobalConstants.KEY_POINT) { _, bundle ->
            id = bundle.getLong(GlobalConstants.KEY_ID)

            if (id == 0L) {
                latitude = bundle.getDouble(GlobalConstants.KEY_LATITUDE)
                longitude = bundle.getDouble(GlobalConstants.KEY_LONGITUDE)
                binding.latitude.editText?.setText(String.format("%.7f", latitude))
                binding.longitude.editText?.setText(String.format("%.7f", longitude))
            } else {
                viewModel.getMarker(id).observe(viewLifecycleOwner) { marker ->
                    latitude = marker.latitude
                    longitude = marker.longitude
                    binding.description.editText?.setText(marker.description)
                    binding.latitude.editText?.setText(String.format("%.7f", latitude))
                    binding.longitude.editText?.setText(String.format("%.7f", longitude))
                }
            }
        }

        binding.btCreate.setOnClickListener {
            val description = binding.description.editText?.text.toString()
            viewModel.edit(Marker(id, description, latitude, longitude))
            viewModel.save()
            viewModel.cancelEdit()
            findNavController().navigateUp()
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            viewModel.cancelEdit()
            findNavController().navigateUp()
        }

        return binding.root
    }
}