package com.example.weather.model

interface Repo {
    fun getWeather(): Weather
    fun getListCities(): List<Weather>
}