package com.example.earthquaker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.earthquaker.Earthqueake

@Dao
interface EqDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(eqList: MutableList<Earthqueake>)

    @Query("SELECT * FROM earthquakes")
    fun getEarthquakes(): MutableList<Earthqueake>

    @Query("SELECT * FROM earthquakes order by magnitude ASC")
    fun getEarthquakesbyMagnitude(): MutableList<Earthqueake>

    //@Delete()

    //@Update()
}