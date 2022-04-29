package ru.maps.markersonyandexmaps.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.ui_view.ViewProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.maps.markersonyandexmaps.R
import ru.maps.markersonyandexmaps.databinding.FragmentMapBinding
import ru.maps.markersonyandexmaps.viewModel.MarkerViewModel

@AndroidEntryPoint
class MapFragment : Fragment() {

    private val viewModel: MarkerViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private var targetLocation = Point(59.945933, 30.320045)
    private lateinit var mapView: MapView
    private lateinit var userLocationLayer: UserLocationLayer
    private lateinit var mapKit: MapKit
    private lateinit var mapObjects: MapObjectCollection

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

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
        mapView = binding.mapview
        mapObjects = mapView.map.mapObjects.addCollection()

        moveToLocation()

        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow).apply {
            isVisible = true
        }
        userLocationLayer.setTapListener { point ->
            Toast.makeText(context, "${point.latitude} x ${point.longitude}", Toast.LENGTH_SHORT)
                .show()
            Log.i("myLocation", "latitude:  ${point.latitude}")
            Log.i("myLocation", "longitude:  ${point.longitude}")
        }

        val point = Point(targetLocation.latitude + 1F, targetLocation.longitude + 1F)
        drawPlacemark(point, mapObjects)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    private fun moveToLocation() {
        mapView.map.move(
            CameraPosition(targetLocation, 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5F),
            null
        )

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        when (PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    targetLocation = Point(location.latitude, location.longitude)
                    mapView.map.move(
                        CameraPosition(targetLocation, 14.0f, 0.0f, 0.0f),
                        Animation(Animation.Type.SMOOTH, 5F),
                        null
                    )
                }
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun drawPlacemark(point: Point, mapObjects: MapObjectCollection) {
        val context = requireActivity().applicationContext
        val view = View(context).apply {
            background = getDrawable(
                context,
                R.drawable.ic_baseline_location_on_24
            )
        }
        mapObjects.addPlacemark(point, ViewProvider(view))
        Log.i("myLocation", "newPoint: ${point.longitude} x ${point.longitude}")
    }

    private fun drawSimpleBitmap(text: String): Bitmap {
        val picSize = 20
        val bitmap = Bitmap.createBitmap(picSize, picSize, Bitmap.Config.ARGB_8888);
        val canvas = Canvas(bitmap)
        // отрисовка плейсмарка
        val paint = Paint()
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL
        canvas.drawCircle(picSize / 2F, picSize / 2F, picSize / 2F, paint);
        // отрисовка текста
        paint.color = Color.WHITE
        paint.isAntiAlias = true
        paint.textSize = 5F
        paint.textAlign = Paint.Align.CENTER;
        canvas.drawText(
            text, picSize / 2F,
            picSize / 2F - ((paint.descent() + paint.ascent()) / 2F), paint
        )
        return bitmap
    }
}
