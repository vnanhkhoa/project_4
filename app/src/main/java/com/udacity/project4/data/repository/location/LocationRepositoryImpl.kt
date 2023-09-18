package com.udacity.project4.data.repository.location

import com.udacity.project4.data.database.entites.Location
import com.udacity.project4.data.datasource.location.LocationDataSource
import com.udacity.project4.data.repository.dto.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationRepositoryImpl(
    private val locationDataSource: LocationDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocationRepository {

    companion object {
        private const val ERROR_MESSAGE = "Location not found"
    }

    override suspend fun add(location: Location) = withContext(dispatcher) {
        locationDataSource.create(location = location)
    }

    override suspend fun getLocations(): Result<List<Location>> = withContext(dispatcher) {
        return@withContext try {
            val locations = locationDataSource.getLocations()
            if (locations.isEmpty()) {
                return@withContext Result.Error(ERROR_MESSAGE)
            }
            Result.Success(locations)
        } catch (ex: Exception) {
            Result.Error(ERROR_MESSAGE)
        }
    }

    override suspend fun getLocation(id: Int): Result<Location> = withContext(dispatcher) {
        return@withContext try {
            val location = locationDataSource.getLocation(id = id)
            Result.Success(location)
        } catch (ex: Exception) {
            Result.Error(ERROR_MESSAGE)
        }
    }

    override suspend fun update(location: Location) = withContext(dispatcher) {
        locationDataSource.update(location = location)
    }

    override suspend fun delete(location: Location) = withContext(dispatcher) {
        locationDataSource.delete(location = location)
    }
}