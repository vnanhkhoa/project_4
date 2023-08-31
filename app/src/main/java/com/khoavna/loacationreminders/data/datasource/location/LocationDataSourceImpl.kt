package com.khoavna.loacationreminders.data.datasource.location

import com.khoavna.loacationreminders.data.database.daos.LocationDao
import com.khoavna.loacationreminders.data.database.entites.Location
import kotlinx.coroutines.flow.Flow

class LocationDataSourceImpl(
    private val locationDao: LocationDao
) : LocationDataSource {

    override suspend fun create(location: Location) {
        locationDao.insert(location)
    }

    override suspend fun create(vararg locations: Location) {
        locationDao.insertAll(*locations)
    }

    override fun getLocations(): Flow<List<Location>> = locationDao.getAll()

    override suspend fun update(location: Location) {
        locationDao.update(location)
    }

    override suspend fun delete(location: Location) {
        locationDao.delete(location)
    }
}