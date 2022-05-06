package ru.maps.markersonyandexmaps.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import dagger.hilt.android.AndroidEntryPoint
import ru.maps.markersonyandexmaps.R
import ru.maps.markersonyandexmaps.application.GlobalConstants
import ru.maps.markersonyandexmaps.databinding.FragmentMapBinding
import ru.maps.markersonyandexmaps.util.attachToLifecycle
import ru.maps.markersonyandexmaps.util.drawPlacemark
import ru.maps.markersonyandexmaps.util.getUserLocation
import ru.maps.markersonyandexmaps.util.moveToLocation
import ru.maps.markersonyandexmaps.viewModel.MarkerViewModel

@AndroidEntryPoint
class MapFragment : Fragment() {

    private val viewModel: MarkerViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private var defaultCameraLocation = Point(59.945933, 30.320045)
    private lateinit var userLocationLayer: UserLocationLayer
    private lateinit var mapKit: MapKit
    private lateinit var mapObjects: MapObjectCollection
    private lateinit var userLocation: Point
    private var isMoveToPoint = false

    private val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            //nothing to do
        }

        override fun onMapLongTap(map: Map, point: Point) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("${context?.getString(R.string.create_marker)}?")
                .setMessage(
                    "${context?.getString(R.string.latitude)}: ${point.latitude}\n" +
                            "${context?.getString(R.string.longitude)}: ${point.longitude}"
                )
                .setNegativeButton(context?.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(context?.getString(R.string.create)) { _, _ ->
                    setFragmentResult(
                        GlobalConstants.KEY_POINT,
                        bundleOf(
                            GlobalConstants.KEY_LATITUDE to point.latitude,
                            GlobalConstants.KEY_LONGITUDE to point.longitude,
                            GlobalConstants.KEY_ID to 0L
                        )
                    )
                    findNavController().navigate(R.id.action_mapFragment_to_editFragment)
                }.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(context)
        mapKit = MapKitFactory.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentMapBinding.inflate(inflater, container, false)
        val mapView = binding.mapview
        mapView.map.addInputListener(inputListener)
        mapObjects = mapView.map.mapObjects.addCollection()

        setFragmentResultListener(GlobalConstants.KEY_CAMERA_POSITION) { _, bundle ->
            val latitude = bundle.getDouble(GlobalConstants.KEY_CAMERA_POSITION_LATITUDE)
            val longitude = bundle.getDouble(GlobalConstants.KEY_CAMERA_POSITION_LONGITUDE)
            moveToLocation(mapView, Point(latitude, longitude))
            isMoveToPoint = true
        }

        getUserLocation(defaultCameraLocation, this).observe(viewLifecycleOwner) {
            userLocation = it
            if (!isMoveToPoint) {
                moveToLocation(mapView, userLocation)
            }
        }

        binding.btToMyLocation.setOnClickListener {
            moveToLocation(mapView, userLocation)
        }

        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow).apply {
            isVisible = true
        }

        viewModel.data.observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                mapView.map.mapObjects.clear()
                mapObjects = mapView.map.mapObjects.addCollection()
                data.forEach { marker ->
                    val point = Point(marker.latitude, marker.longitude)
                    drawPlacemark(point, mapObjects)
                }
            }
        }

        mapView.attachToLifecycle(viewLifecycleOwner)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}
