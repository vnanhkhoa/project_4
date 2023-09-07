package com.khoavna.loacationreminders.ui.locationdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khoavna.loacationreminders.data.database.entites.Location
import com.khoavna.loacationreminders.domain.location.LocationUseCase
import com.khoavna.loacationreminders.ui.dto.LocationSelectDto
import kotlinx.coroutines.launch

class LocationDetailViewModel(
    private val locationUseCase: LocationUseCase
) : ViewModel() {

    var location: Location? = null
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val locationDto = MutableLiveData<LocationSelectDto>()

    fun saveLocation() {
        viewModelScope.launch {
            location?.also {
                locationUseCase.update(
                    it.copy(
                        title = title.value.orEmpty(),
                        description = description.value.orEmpty(),
                        locationName = locationDto.value?.name ?: "",
                        longitude = locationDto.value?.longitude ?: 0.0,
                        latitude = locationDto.value?.latitude ?: 0.0
                    )
                )

                return@launch
            }
            locationUseCase.create(
                Location(
                    title = title.value.orEmpty(),
                    description = description.value.orEmpty(),
                    locationName = locationDto.value?.name ?: "",
                    longitude = locationDto.value?.longitude ?: 0.0,
                    latitude = locationDto.value?.latitude ?: 0.0
                )
            )
        }
    }
}