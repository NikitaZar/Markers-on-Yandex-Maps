package ru.maps.markersonyandexmaps.hiltModules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.maps.markersonyandexmaps.dao.MarkerDao
import ru.maps.markersonyandexmaps.db.AppDb

@InstallIn(SingletonComponent::class)
@Module
object DaoModule {
    @Provides
    fun provideMarkerDao(db: AppDb): MarkerDao = db.markerDao()
}