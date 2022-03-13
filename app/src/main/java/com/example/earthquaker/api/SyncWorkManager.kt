package com.example.earthquaker.api

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.earthquaker.database.getDatabase
import com.example.earthquaker.main.MainRepository

//Aquì se realizan las tareas
//En el otro son las configuraciones con las que va corre esa tarea
// clase hereda de CoroutineWorker
class SyncWorkManager(appContext: Context, params: WorkerParameters): CoroutineWorker (appContext, params){
    companion object{
        const val WORK_NAME = "SyncWorkManager"
    }

    private val database = getDatabase(appContext)
    private val repository = MainRepository(database)

    override suspend fun doWork(): Result {
        //Tarea de sincronizar la informacion de la api hacia la bd
        repository.fetchEarthquakes(true)

        return Result.success()
    }
}