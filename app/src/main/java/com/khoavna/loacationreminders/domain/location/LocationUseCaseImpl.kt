package com.khoavna.loacationreminders.domain.location

import com.khoavna.loacationreminders.data.database.entites.Location
import com.khoavna.loacationreminders.data.repository.location.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationUseCaseImpl(private val locationRepository: LocationRepository) : LocationUseCase {
    override suspend fun create(location: Location) {
        locationRepository.add(location)
    }

    override suspend fun create(vararg location: Location) {
        locationRepository.add(*location)
    }

    override fun getLocations(): Flow<List<Location>> = locationRepository.getLocations()

    override suspend fun update(location: Location) {
        locationRepository.update(location)
    }

    override suspend fun delete(location: Location) {
        locationRepository.delete(location)
    }
}