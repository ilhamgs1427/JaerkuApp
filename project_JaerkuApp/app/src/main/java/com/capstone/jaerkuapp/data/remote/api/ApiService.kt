package com.capstone.jaerkuapp.data.remote.api

import com.capstone.jaerkuapp.data.remote.response.FuzzyResponse
import com.capstone.jaerkuapp.data.remote.response.SensorResponse
import retrofit2.http.GET

interface ApiService {
    @GET("getDataFuzzy.php")
    suspend fun getDataFuzzy(): FuzzyResponse

    @GET("getDataSensor.php")
    suspend fun getListSensor(): SensorResponse
}