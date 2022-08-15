package com.example.weather.model.repo

import com.example.weather.model.getRussianCities
import com.example.weather.model.getWorldCities

class RepoCitiesListImpl: RepoCitiesList {

    override fun getRussianCitiesListFromLocal() = getRussianCities()

    override fun getWorldCitiesListFromLocal() = getWorldCities()

}