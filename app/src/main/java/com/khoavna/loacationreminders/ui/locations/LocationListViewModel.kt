package com.khoavna.loacationreminders.ui.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khoavna.loacationreminders.data.database.entites.Location
import com.khoavna.loacationreminders.domain.location.LocationUseCase
import kotlinx.coroutines.launch

class LocationListViewModel(
    private val locationUseCase: LocationUseCase
) : ViewModel() {

    private val _locations = MutableLiveData<List<Location>>()
    val locations: LiveData<List<Location>> = _locations

    fun getLocation() {
        viewModelScope.launch {
            locationUseCase.getLocations().collect {
                _locations.value = it
            }
        }
    }
}