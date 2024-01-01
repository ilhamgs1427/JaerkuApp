package com.capstone.jaerkuapp.data.remote.response

import androidx.lifecycle.LiveData
import com.google.gson.annotations.SerializedName

data class FuzzyResponse(

	@field:SerializedName("sensorData")
	val sensorData: SensorData,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class SensorData(

	@field:SerializedName("pH_Value")
	val pHValue: String? = null,

	@field:SerializedName("Turbidity_Value")
	val turbidityValue: String? = null,

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("kondisi")
	val kondisi: String? = null,

	@field:SerializedName("TDS_Value")
	val tDSValue: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("fuzzy")
	val fuzzy: String? = null
)
