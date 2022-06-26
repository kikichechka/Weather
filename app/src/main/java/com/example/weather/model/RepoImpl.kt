package com.example.weather.model

class RepoImpl():Repo {
    override fun getWeather(): Weather {
        return defaultCity()
    }

    override fun getListCities(): List<Weather> {
        return listOf()
    }

}