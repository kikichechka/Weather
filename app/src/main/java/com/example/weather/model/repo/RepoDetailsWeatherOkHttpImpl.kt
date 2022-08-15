package com.example.weather.model.repo

import com.example.weather.BuildConfig
import com.example.weather.model.clickCity
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.utils.KEY_API
import com.example.weather.utils.MESSAGE_ERROR
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class RepoDetailsWeatherOkHttpImpl : RepoDetailsWeather {
    override fun getWeather(lat: Double, lon: Double, callback: MyCallback) {
        val city = clickCity(lat, lon)
        val client = OkHttpClient()
        val builder = Request.Builder()
        builder.addHeader(KEY_API, BuildConfig.WEATHER_API_KEY)
        builder.url("https://api.weather.yandex.ru/v2/informers?lat=${city.lat}&lon=${city.lon}")
        val request: Request = builder.build()
        val call: Call = client.newCall(request)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.error(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val weatherDTO = Gson().fromJson((it.string()), WeatherDTO::class.java)
                        callback.onResponse(weatherDTO, city.name)
                    }
                } else {
                    callback.error(IOException(MESSAGE_ERROR))
                }
            }
        })
    }
}