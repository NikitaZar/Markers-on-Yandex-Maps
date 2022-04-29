package ru.maps.markersonyandexmaps.viewModel

import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.maps.markersonyandexmaps.dto.Marker
import ru.maps.markersonyandexmaps.model.FeedModel
import ru.maps.markersonyandexmaps.repository.MarkerRepository
import ru.maps.markersonyandexmaps.repository.MarkerRepositoryImpl
import ru.maps.markersonyandexmaps.util.SingleLiveEvent
import javax.inject.Inject

private val empty = Marker(
    id = 0L,
    description = "",
    latitude = 0.0,
    longitude = 0.0
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MarkerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MarkerRepository
) : ViewModel() {

    val data: LiveData<FeedModel> =
        repository.data.map {
            FeedModel(it)
        }.asLiveData(Dispatchers.Default)

    private val edited = MutableLiveData(empty)

    private val _markerCreated = SingleLiveEvent<Unit>()

    val markerCreated: LiveData<Unit>
        get() = _markerCreated

    fun save() = viewModelScope.launch {
        edited.value?.let { repository.save(it) }
        edited.value = empty
    }

    fun edit(marker: Marker) {
        edited.value = marker
    }

    fun cancelEdit() {
        edited.value = empty
    }

    fun remove(id: Long) = viewModelScope.launch {
        repository.remove(id)
    }

    fun getMarker(id: Long) = viewModelScope.launch {
        repository.getMarker(id)
    }
}
