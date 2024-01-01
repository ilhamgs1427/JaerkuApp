package com.capstone.jaerkuapp.data.repository

import androidx.lifecycle.LiveData
import com.capstone.jaerkuapp.data.remote.DataResult
import com.capstone.jaerkuapp.data.remote.api.ApiService
import com.capstone.jaerkuapp.data.remote.response.DataItem
import com.capstone.jaerkuapp.data.remote.response.FuzzyResponse
import com.capstone.jaerkuapp.data.remote.response.SensorResponse

class SensorRepository(private val apiService: ApiService) {
    suspend fun getFuzzySensor(): FuzzyResponse {
        return apiService.getDataFuzzy()
    }
    suspend fun getListSensor(): DataResult<List<DataItem>> {
        return try {
            val response = apiService.getListSensor()
            DataResult.Success(response.dataItem)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "${e.message}")
        }
    }
}