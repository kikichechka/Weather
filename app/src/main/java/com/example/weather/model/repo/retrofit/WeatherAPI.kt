package com.example.weather.model.repo.retrofit

import com.example.weather.model.dto.WeatherDTO
import com.example.weather.utils.KEY_API
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherAPI {

    @GET("/v2/informers")
    fun getWeather(
        @Header(KEY_API) keyValue: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<WeatherDTO>
}