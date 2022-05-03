package ru.maps.markersonyandexmaps.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.maps.markersonyandexmaps.dao.MarkerDao
import ru.maps.markersonyandexmaps.entity.MarkerEntity

@Database(entities = [MarkerEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun markerDao(): MarkerDao
}