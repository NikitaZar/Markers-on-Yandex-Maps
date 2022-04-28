package ru.maps.markersonyandexmaps.hiltModules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.maps.markersonyandexmaps.repository.MarkerRepository
import ru.maps.markersonyandexmaps.repository.MarkerRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MarkerRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMarkerRepository(imp: MarkerRepositoryImpl): MarkerRepository
}