package ru.maps.markersonyandexmaps.viewModel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
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
    id = 0,
    description = "",
    latitude = 0F,
    longitude = 0F
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MarkerViewModel @Inject constructor(
    private val repository: MarkerRepository
) : ViewModel() {

    val data: LiveData<FeedModel> =
        repository.data.map {
            FeedModel(it)
        }.asLiveData(Dispatchers.Default)

    private val edited = MutableLiveData(empty)

    private val _postCreated = SingleLiveEvent<Unit>()

    val postCreated: LiveData<Unit>
        get() = _postCreated


    fun save() = viewModelScope.launch {
        edited.value?.let { repository.save(it) }
    }

    fun edit(marker: Marker) {
        edited.value = marker
    }

    fun remove(id: Long) = viewModelScope.launch {
        repository.remove(id)
    }

    fun getMarker(id: Long) = viewModelScope.launch {
        repository.getMarker(id)
    }
}
