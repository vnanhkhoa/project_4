package com.udacity.project4.domain.location

import com.udacity.project4.data.database.entites.Location
import com.udacity.project4.data.repository.dto.Result
import kotlinx.coroutines.flow.Flow

interface LocationUseCase {
    suspend fun create(location: Location): Long

    fun getLocations(): Flow<List<Location>>
    suspend fun getLocation(id: Int): Result<Location>

    suspend fun update(location: Location)

    suspend fun delete(location: Location)
}