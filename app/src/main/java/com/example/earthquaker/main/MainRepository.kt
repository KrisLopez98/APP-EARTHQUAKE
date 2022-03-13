package com.example.earthquaker.main

import com.example.earthquaker.Earthqueake
import com.example.earthquaker.api.EqJsonResponse
import com.example.earthquaker.api.service
import com.example.earthquaker.database.EqDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// El repositorio tiene relacion con el API y con la BD
class MainRepository (private val database: EqDatabase){

    // Metodo - Sincroniza la informacion de la respuesta del API hacia la
    // Base de Datos
    suspend fun fetchEarthquakes(sortByMagnitude: Boolean): MutableList<Earthqueake> {
        return withContext(Dispatchers.IO) {
            // Hacer uso del api
            val eqJsonResponse = service.getLastHourEarthquakes()
            // Realiza la transformación de objetos EqJsonResponse a objetos Earquake
            val eqList = parseEqResult(eqJsonResponse)
            // Realiza la insercion de temblores a la tabla earqueakes de la BD
            database.eqDao.insertAll(eqList)
            // Obtener la informacion de los temblores de la tabla Earquakes de la Base de datos Earquake
            fetchEarthquakesDataBase(sortByMagnitude)
        }
    }

    // Metodo
    //  Obtener los temblores de la Base de Datos
    suspend fun fetchEarthquakesDataBase(sortByMagnitude: Boolean): MutableList<Earthqueake>{
        return withContext(Dispatchers.IO) {
            if (sortByMagnitude) {
                // Metodo de Base de dATOS
                database.eqDao.getEarthquakesbyMagnitude()
            } else {
                // Metodo de base de datos
                database.eqDao.getEarthquakes()
            }
        }
    }

    // Metodo
    private fun parseEqResult(eqJsonResponse: EqJsonResponse): MutableList<Earthqueake>{
        val eqList = mutableListOf<Earthqueake>()
        val featureList = eqJsonResponse.features

        for (feature in featureList) {
            val properties = feature.properties

            val id = feature.id
            val magnitude = properties.mag
            val place = properties.place
            val time = properties.time

            val geometry = feature.geometry
            val longitude = geometry.longitude
            val latitude = geometry.latitude

            eqList.add(Earthqueake(id, place, magnitude, time, longitude, latitude))
        }

        return eqList
    }
}