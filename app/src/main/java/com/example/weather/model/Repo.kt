package com.example.weather.model

interface Repo {
    fun getWeather(): Weather
    fun getRussianCitiesList(): List<Weather>
    fun getWorldCitiesList(): List<Weather>
}