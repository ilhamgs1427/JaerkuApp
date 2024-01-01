package com.capstone.jaerkuapp.data.di

import android.content.Context
import com.capstone.jaerkuapp.data.remote.api.ApiConfig
import com.capstone.jaerkuapp.data.repository.SensorRepository

object Injection {
    fun provideSensorRepository(context: Context): SensorRepository {
        val apiService = ApiConfig.getApiService(context)
        return SensorRepository(apiService)
    }
}