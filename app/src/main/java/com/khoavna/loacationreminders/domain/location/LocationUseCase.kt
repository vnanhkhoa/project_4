package com.khoavna.loacationreminders.domain.location

import com.khoavna.loacationreminders.data.database.entites.Location
import kotlinx.coroutines.flow.Flow

interface LocationUseCase {
    suspend fun create(location: Location): Long

    suspend fun create(vararg location: Location)

    fun getLocations(): Flow<List<Location>>

    suspend fun update(location: Location)

    suspend fun delete(location: Location)
}