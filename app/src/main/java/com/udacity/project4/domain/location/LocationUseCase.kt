package com.udacity.project4.domain.location

import com.udacity.project4.data.database.entites.Location
import com.udacity.project4.data.repository.dto.Result

interface LocationUseCase {
    suspend fun create(location: Location)
    suspend fun getLocations(): Result<List<Location>>
    suspend fun getLocation(id: Int): Result<Location>

    suspend fun update(location: Location)

    suspend fun delete(location: Location)
}