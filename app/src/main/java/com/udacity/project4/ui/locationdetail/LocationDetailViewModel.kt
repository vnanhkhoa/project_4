package com.udacity.project4.ui.locationdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.Geofence
import com.udacity.project4.data.database.entites.Location
import com.udacity.project4.domain.location.LocationUseCase
import com.udacity.project4.ui.dto.LocationSelectDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationDetailViewModel(
    private val locationUseCase: LocationUseCase
) : ViewModel() {

    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val locationDto = MutableLiveData<LocationSelectDto>()
    private val _location: MutableStateFlow<Location?> = MutableStateFlow(null)
    private var isUpdate = false

    fun saveLocation(callBack: (geofence: Geofence) -> Unit) {
        viewModelScope.launch {
            updateLocation()
            _location.value?.let {
                if (isUpdate) {
                    locationUseCase.update(it)
                    callBack.invoke(createGeofence(it))
                    return@let
                }
                val id = locationUseCase.create(it)
                it.copy(id = id.toInt()).let { result ->
                    _location.value = result
                    callBack.invoke(createGeofence(it))
                }

            }
        }
    }

    fun deleteLocation() {
        viewModelScope.launch {
            _location.value?.let {
                locationUseCase.delete(it)
            }
        }
    }

    fun setLocation(location: Location) {
        _location.value = location
    }

    fun setUpdate(isUpdate: Boolean) {
        this.isUpdate = isUpdate
    }

    private fun createGeofence(location: Location) = location.run {
        Geofence.Builder().setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER).setRequestId(id.toString())
            .setCircularRegion(latitude, longitude, 150f).build()
    }

    private fun updateLocation() {
        _location.update {
            it?.copy(
                title = title.value.orEmpty(),
                description = description.value.orEmpty(),
                locationName = locationDto.value?.name ?: "",
                longitude = locationDto.value?.longitude ?: 0.0,
                latitude = locationDto.value?.latitude ?: 0.0
            ) ?: Location(
                title = title.value.orEmpty(),
                description = description.value.orEmpty(),
                locationName = locationDto.value?.name ?: "",
                longitude = locationDto.value?.longitude ?: 0.0,
                latitude = locationDto.value?.latitude ?: 0.0
            )
        }
    }
}