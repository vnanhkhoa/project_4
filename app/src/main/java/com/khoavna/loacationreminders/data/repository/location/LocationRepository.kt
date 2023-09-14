package com.khoavna.loacationreminders.data.repository.location

import com.khoavna.loacationreminders.data.database.entites.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun add(location: Location): Long

    fun getLocations(): Flow<List<Location>>
    suspend fun getLocation(id: Int): Location

    suspend fun update(location: Location)

    suspend fun delete(location: Location)
}