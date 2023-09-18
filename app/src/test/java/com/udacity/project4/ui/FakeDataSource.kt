package com.udacity.project4.ui

import com.udacity.project4.data.database.entites.Location
import com.udacity.project4.data.repository.dto.Result
import com.udacity.project4.data.repository.location.LocationRepository

class FakeDataSource : LocationRepository {

    private val locations = mutableListOf<Location>()
    private var isError = false
    fun setError(tmp: Boolean) {
        isError = tmp
    }

    override suspend fun add(location: Location) {
        locations.add(location)
    }

    override suspend fun getLocations(): Result<List<Location>> {
        return if (isError) {
            Result.Error("Location not found")
        } else {
            Result.Success(locations)
        }
    }

    override suspend fun getLocation(id: Int): Result<Location> {
        return if (isError) {
            Result.Error("Location not found")
        } else {
            val location = locations.find { it.id == id }
            if (location == null) {
                Result.Error("Location not found")
            } else {
                Result.Success(location)
            }
        }
    }

    override suspend fun update(location: Location) {
        // Don't something
    }

    override suspend fun delete(location: Location) {
        locations.remove(location)
    }

    fun deleteAll() {
        locations.clear()
    }
}