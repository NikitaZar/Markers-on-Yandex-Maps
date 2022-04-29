package ru.maps.markersonyandexmaps.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
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
    private val viewModel: MarkerViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditBinding.inflate(layoutInflater)

        setFragmentResultListener(GlobalConstants.KEY_POINT) { _, bundle ->
            latitude = bundle.getDouble(GlobalConstants.KEY_LATITUDE)
            longitude = bundle.getDouble(GlobalConstants.KEY_LONGITUDE)
            binding.latitude.editText?.setText(String.format("%.7f", latitude))
            binding.longitude.editText?.setText(String.format("%.7f", longitude))
        }

        val description = binding.description.text.toString()

        binding.btCreate.setOnClickListener {
            viewModel.edit(Marker(0, description, latitude, longitude))
            viewModel.save()
            findNavController().navigateUp()
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            viewModel.cancelEdit()
            findNavController().navigateUp()
        }

        return binding.root
    }
}