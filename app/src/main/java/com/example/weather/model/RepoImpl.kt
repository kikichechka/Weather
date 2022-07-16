package com.example.weather.model

class RepoImpl:Repo {

    override fun getFavoriteWeather() = defaultCity()

    override fun getClickWeather(cityName: String) = clickCity(cityName)

    override fun getRussianCitiesListFromLocal() = getRussianCities()
    override fun getRussianCitiesListFromServer() = getRussianCities()

    override fun getWorldCitiesListFromLocal() = getWorldCities()
    override fun getWorldCitiesListFromServer() = getWorldCities()

}