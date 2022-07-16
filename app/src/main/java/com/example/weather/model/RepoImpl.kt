package com.example.weather.model

import android.os.Looper
import android.preference.PreferenceActivity
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.utils.KEY_API
import com.example.weather.utils.KEY_VALUE
import com.example.weather.utils.getLines
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.logging.Handler
import javax.net.ssl.HttpsURLConnection

class RepoImpl:Repo {

    override fun getFavoriteWeather() = defaultCity()

    override fun getClickWeather(cityName: String) = clickCity(cityName)

    override fun getRussianCitiesListFromLocal() = getRussianCities()
    override fun getRussianCitiesListFromServer() = getRussianCities()

    override fun getWorldCitiesListFromLocal() = getWorldCities()
    override fun getWorldCitiesListFromServer() = getWorldCities()

}