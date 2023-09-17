package com.udacity.project4.data.datasource.location

import com.udacity.project4.data.database.entites.Location
import kotlinx.coroutines.flow.Flow

interface LocationDataSource {

    suspend fun create(location: Location): Long

    fun getLocations(): Flow<List<Location>>

    suspend fun getLocation(id: Int): Location

    suspend fun update(location: Location)

    suspend fun delete(location: Location)
}