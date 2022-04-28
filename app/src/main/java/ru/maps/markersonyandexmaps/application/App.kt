package ru.maps.markersonyandexmaps.application

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp
import ru.maps.markersonyandexmaps.R

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val MAPKIT_API_KEY = applicationContext.getString(R.string.MAPKIT_API_KEY)
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
    }
}