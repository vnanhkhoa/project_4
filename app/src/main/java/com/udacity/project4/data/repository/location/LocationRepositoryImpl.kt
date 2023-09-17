package com.udacity.project4.data.repository.location

import com.udacity.project4.data.database.entites.Location
import com.udacity.project4.data.datasource.location.LocationDataSource
import com.udacity.project4.data.repository.dto.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocationRepositoryImpl(
    private val locationDataSource: LocationDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocationRepository {
    override suspend fun add(location: Location) = withContext(dispatcher) {
        val id = async { locationDataSource.create(location = location) }
        return@withContext id.await()
    }

    override fun getLocations(): Flow<List<Location>> = locationDataSource.getLocations()
    override suspend fun getLocation(id: Int): Result<Location> = withContext(dispatcher) {
        return@withContext try {
            Result.Success(locationDataSource.getLocation(id = id))
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }

    }

    override suspend fun update(location: Location) = withContext(dispatcher) {
        locationDataSource.update(location = location)
    }

    override suspend fun delete(location: Location) = withContext(dispatcher) {
        locationDataSource.delete(location = location)
    }
}