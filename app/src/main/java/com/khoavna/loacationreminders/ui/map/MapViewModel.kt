package com.khoavna.loacationreminders.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PointOfInterest
import com.khoavna.loacationreminders.ui.dto.LocationSelectDto

class MapViewModel : ViewModel() {

    var locationSelectDto: LocationSelectDto? = null
        private set
    private var marker: Marker? = null
    private var pointOfInterest: PointOfInterest? = null

    private val _styleMap = MutableLiveData(GoogleMap.MAP_TYPE_NORMAL)
    val styleMap: LiveData<Int> = _styleMap

    private fun removeMarker() {
        marker?.remove()
        marker = null
    }

    fun updateMarkerAndPointOfInterest(marker: Marker, pointOfInterest: PointOfInterest) {
        removeMarker()
        marker.showInfoWindow()
        this.pointOfInterest = pointOfInterest
        this.marker = marker
        updateLocationDto()
    }

    private fun updateLocationDto() {
        pointOfInterest?.let {
            locationSelectDto = LocationSelectDto(
                name = it.name,
                longitude = it.latLng.longitude,
                latitude = it.latLng.latitude
            )
        }

    }

    fun updateMapStyle(style: Int): Boolean {
        if (style != -1) _styleMap.value = style
        return style != -1
    }

}