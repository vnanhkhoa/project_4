package com.khoavna.loacationreminders.data.repository.location

import com.khoavna.loacationreminders.data.database.entites.Location
import com.khoavna.loacationreminders.data.datasource.location.LocationDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocationRepositoryImpl(
    private val locationDataSource: LocationDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocationRepository {
    override suspend fun add(location: Location) = withContext(dispatcher) {
        locationDataSource.create(location = location)
    }

    override suspend fun add(vararg location: Location) = withContext(dispatcher) {
        locationDataSource.create(location = location)
    }

    override fun getLocations(): Flow<List<Location>> = locationDataSource.getLocations()

    override suspend fun update(location: Location) = withContext(dispatcher) {
        locationDataSource.update(location = location)
    }

    override suspend fun delete(location: Location) = withContext(dispatcher) {
        locationDataSource.delete(location = location)
    }
}