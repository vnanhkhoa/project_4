package com.udacity.project4.data.datasource.location

import com.udacity.project4.data.database.daos.LocationDao
import com.udacity.project4.data.database.entites.Location
import kotlinx.coroutines.flow.Flow

class LocationDataSourceImpl(
    private val locationDao: LocationDao
) : LocationDataSource {

    override suspend fun create(location: Location) = locationDao.insert(location)

    override fun getLocations(): Flow<List<Location>> = locationDao.getAll()

    override suspend fun getLocation(id: Int): Location = locationDao.getLocation(id)

    override suspend fun update(location: Location) {
        locationDao.update(location)
    }

    override suspend fun delete(location: Location) {
        locationDao.delete(location)
    }
}