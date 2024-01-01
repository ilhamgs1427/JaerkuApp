package com.capstone.jaerkuapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.jaerkuapp.data.remote.DataResult
import com.capstone.jaerkuapp.data.remote.response.DataItem
import com.capstone.jaerkuapp.data.remote.response.FuzzyResponse
import com.capstone.jaerkuapp.data.remote.response.SensorResponse
import com.capstone.jaerkuapp.data.repository.SensorRepository
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: SensorRepository) : ViewModel() {
    private val _fuzzyData = MutableLiveData<FuzzyResponse>()
    val fuzzyData: LiveData<FuzzyResponse>
        get() = _fuzzyData

    private val _sensor = MutableLiveData<SensorResponse>()
    val sensor: LiveData<SensorResponse>
        get() = _sensor
    private val _sensorList = MutableLiveData<DataResult<List<DataItem>>>()
    val sensorList: LiveData<DataResult<List<DataItem>>> get() = _sensorList
    fun getFuzzyData() {
        viewModelScope.launch {
            try {
                val response = repository.getFuzzySensor()
                _fuzzyData.value = response
            } catch (e: Exception) {
                Log.d("DashboardViewModel", "error : ${e.message.toString()}")
            }
        }
    }
    fun getSensorList() {
        viewModelScope.launch {
            _sensorList.value = DataResult.Loading
            _sensorList.value = repository.getListSensor()
        }
    }
}