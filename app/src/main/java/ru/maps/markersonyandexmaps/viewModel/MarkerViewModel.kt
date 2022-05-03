package ru.maps.markersonyandexmaps.viewModel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.maps.markersonyandexmaps.dto.Marker
import ru.maps.markersonyandexmaps.repository.MarkerRepository
import ru.maps.markersonyandexmaps.util.SingleLiveEvent
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MarkerViewModel @Inject constructor(
    private val repository: MarkerRepository
) : ViewModel() {

    val data: LiveData<List<Marker>> =
        repository.data.asLiveData(Dispatchers.Default)

    val edited = MutableLiveData(empty)

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

    fun getMarker(id: Long): LiveData<Marker> {
        val marker = MutableLiveData<Marker>()
        viewModelScope.launch {
            marker.postValue(repository.getMarker(id))
        }
        return marker
    }


}

private val empty = Marker(
    id = 0L,
    description = "",
    latitude = 0.0,
    longitude = 0.0
)

