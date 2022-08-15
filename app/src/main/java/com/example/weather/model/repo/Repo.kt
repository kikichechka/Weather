package com.example.weather.model.repo

import com.example.weather.model.Weather
import com.example.weather.model.dto.WeatherDTO
import java.io.IOException

interface RepoCitiesList {
    fun getRussianCitiesListFromLocal(): List<Weather>
    fun getWorldCitiesListFromLocal(): List<Weather>
}

interface RepoFavoriteWeather {
    fun getFavoriteWeather(): Weather
}

interface MyCallback {
    fun onResponse(weatherDTO: WeatherDTO, cityName: String)
    fun error(e: IOException)
}

interface RepoDetailsWeather {
    fun getWeather(lat: Double, lon: Double, callback: MyCallback)
}
