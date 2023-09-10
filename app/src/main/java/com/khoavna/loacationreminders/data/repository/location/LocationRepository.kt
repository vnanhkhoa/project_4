package com.khoavna.loacationreminders.data.repository.location

import com.khoavna.loacationreminders.data.database.entites.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun add(location: Location): Long

    suspend fun add(vararg location: Location)

    fun getLocations(): Flow<List<Location>>

    suspend fun update(location: Location)

    suspend fun delete(location: Location)
}