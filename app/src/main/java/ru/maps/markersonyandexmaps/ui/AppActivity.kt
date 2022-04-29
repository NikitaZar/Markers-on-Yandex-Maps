package ru.maps.markersonyandexmaps.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.maps.markersonyandexmaps.R
import ru.maps.markersonyandexmaps.databinding.ActivityAppBinding

@AndroidEntryPoint
class AppActivity : AppCompatActivity(R.layout.activity_app) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAppBinding.inflate(layoutInflater)

        binding.bottomNavigation.setOnItemReselectedListener { item ->
            Log.i("navigate", "${item.itemId}")
            val fragmentId = when (item.itemId) {
                R.id.page_list -> R.id.listFragment
                R.id.page_map -> R.id.mapFragment
                else -> null
            }
            fragmentId?.let {
                findNavController(R.id.nav_host_fragment).navigate(
                    it,
                    null,
                    NavOptions.Builder().setLaunchSingleTop(true).build()
                )
            }
        }
    }
}