package com.example.earthquaker

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize



//  Model
//ENTITY REPRESENTA UNA TABLA DE LA BASE DE DATOS

@Parcelize
@Entity(tableName = "earthquakes")
 data class Earthqueake (@PrimaryKey val id: String, val place: String, val magnitude: Double, val time: Long,
                         val longitude: Double, val latitude: Double) : Parcelable