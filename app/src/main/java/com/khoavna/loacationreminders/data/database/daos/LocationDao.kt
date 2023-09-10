package com.khoavna.loacationreminders.data.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.khoavna.loacationreminders.data.database.entites.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: Location): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg location: Location)

    @Query("Select * from `location`")
    fun getAll(): Flow<List<Location>>

    @Update
    suspend fun update(location: Location)

    @Delete
    suspend fun delete(location: Location)
}