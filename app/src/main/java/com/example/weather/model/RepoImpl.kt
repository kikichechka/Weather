package com.example.weather.model

class RepoImpl():Repo {
    override fun getWeather() = defaultCity()

    override fun getRussianCitiesList() = getRussianCities()

    override fun getWorldCitiesList() = getWorldCities()

}