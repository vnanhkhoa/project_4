package com.udacity.project4.domain.location

import com.udacity.project4.data.database.entites.Location
import com.udacity.project4.data.repository.location.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationUseCaseImpl(private val locationRepository: LocationRepository) : LocationUseCase {
    override suspend fun create(location: Location) = locationRepository.add(location)

    override fun getLocations(): Flow<List<Location>> = locationRepository.getLocations()
    override suspend fun getLocation(id: Int): Location = locationRepository.getLocation(id)

    override suspend fun update(location: Location) {
        locationRepository.update(location)
    }

    override suspend fun delete(location: Location) {
        locationRepository.delete(location)
    }
}