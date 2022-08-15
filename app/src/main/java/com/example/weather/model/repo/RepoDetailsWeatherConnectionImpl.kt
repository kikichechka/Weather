package com.example.weather.model.repo

import com.example.weather.BuildConfig
import com.example.weather.model.clickCity
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.utils.KEY_API
import com.example.weather.utils.getLines
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RepoDetailsWeatherConnectionImpl : RepoDetailsWeather {
    override fun getWeather(lat: Double, lon: Double, callback: MyCallback) {
        val city = clickCity(lat, lon)
        Thread {
            val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
            val myConnection: HttpsURLConnection?
            myConnection = uri.openConnection() as HttpsURLConnection
            try {
                myConnection.addRequestProperty(KEY_API, BuildConfig.WEATHER_API_KEY)

                val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
                callback.onResponse(weatherDTO, city.name)
            }catch (e: IOException){
                callback.error(e)
            }finally {
                myConnection.disconnect()
            }
        }.start()
    }
}