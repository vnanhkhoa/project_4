package com.udacity.project4.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.udacity.project4.data.database.entites.Location

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: Location)

    @Query("Select * from `location`")
    suspend fun getAll(): List<Location>

    @Query("Select * from `location` where `id` = :id")
    suspend fun getLocation(id: Int): Location

    @Update
    suspend fun update(location: Location)

    @Delete
    suspend fun delete(location: Location)
}