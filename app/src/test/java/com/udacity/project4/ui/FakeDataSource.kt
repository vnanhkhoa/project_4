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
            try {
                if (locations.isEmpty()) {
                    Result.Error("Empty")
                } else {
                    Result.Success(locations)
                }
            } catch (e: Exception) {
                Result.Error("GetLocations Error")
            }
        }
    }

    override suspend fun getLocation(id: String): Result<Location> {
        return if (isError) {
            Result.Error("GetLocation Error")
        } else {
            try {
                val location = locations.find { it.id == id }
                if (location == null) {
                    Result.Error("Location not found")
                } else {
                    Result.Success(location)
                }
            } catch (e: Exception) {
                Result.Error("GetLocation Error")
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