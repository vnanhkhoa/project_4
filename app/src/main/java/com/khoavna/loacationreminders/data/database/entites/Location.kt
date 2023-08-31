package com.khoavna.loacationreminders.data.database.entites

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Location(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val description: String = "",
    val location: String = "",
    val x: Double = 0.0,
    val y: Double = 0.0,
) : Parcelable
