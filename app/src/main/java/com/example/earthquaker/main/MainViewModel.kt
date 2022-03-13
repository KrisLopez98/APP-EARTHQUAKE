package com.example.earthquaker.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.earthquaker.Earthqueake
import com.example.earthquaker.api.ApiResponseStatus
import com.example.earthquaker.database.getDatabase
import kotlinx.coroutines.launch
import java.net.UnknownHostException

private val TAG = MainViewModel::class.java.simpleName
class MainViewModel(application: Application, private val sortType: Boolean): AndroidViewModel(application) {

    private val database = getDatabase(application.applicationContext)
    // Propiedad de la Clase
    private val repository = MainRepository(database)

    private val _status = MutableLiveData<ApiResponseStatus>()
    val status: LiveData<ApiResponseStatus>
        get() = _status

    private val _eqList = MutableLiveData<MutableList<Earthqueake>>()
    val eqList: LiveData<MutableList<Earthqueake>>
        get() = _eqList

    // Constructor
    init {
        reloadEarthquakesFromDataBase(sortType)
    }

    // Metodo
    fun reloadEarqueakes() {
        viewModelScope.launch {
            try {
                _status.value = ApiResponseStatus.LOADING
                _eqList.value = repository.fetchEarthquakes(sortType)
                _status.value = ApiResponseStatus.DONE
            } catch (e: UnknownHostException) {
                _status.value = ApiResponseStatus.NOT_INTERNET_CONNECTION
                Log.d(TAG, "No internet connection", e)
            }
        }
    }

    // Metodo
    fun reloadEarthquakesFromDataBase(sortByMagnitude: Boolean){
        viewModelScope.launch {
            _eqList.value = repository.fetchEarthquakesDataBase(sortByMagnitude)
            if (_eqList.value!!.isEmpty()){
                reloadEarqueakes()
            }
        }
    }
}

