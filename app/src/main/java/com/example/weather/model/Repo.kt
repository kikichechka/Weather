package com.example.weather.model

interface Repo {
    fun getFavoriteWeather(): Weather
    fun getClickWeather(cityName: String): Weather
    fun getRussianCitiesListFromLocal(): List<Weather>
    fun getRussianCitiesListFromServer(): List<Weather>
    fun getWorldCitiesListFromLocal(): List<Weather>
    fun getWorldCitiesListFromServer(): List<Weather>
}