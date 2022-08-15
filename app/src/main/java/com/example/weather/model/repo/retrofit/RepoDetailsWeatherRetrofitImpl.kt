package com.example.weather.model.repo.retrofit


import com.example.weather.BuildConfig
import com.example.weather.model.clickCity
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.model.repo.MyCallback
import com.example.weather.model.repo.RepoDetailsWeather
import com.example.weather.utils.MESSAGE_ERROR
import com.google.gson.GsonBuilder
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RepoDetailsWeatherRetrofitImpl : RepoDetailsWeather {

    override fun getWeather(lat: Double, lon: Double, callback: MyCallback) {
        val retrofitImpl = Retrofit.Builder()
        val baseUrl = "https://api.weather.yandex.ru"
        retrofitImpl.baseUrl(baseUrl)
        retrofitImpl.addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        val api = retrofitImpl.build().create(WeatherAPI::class.java)
        api.getWeather(BuildConfig.WEATHER_API_KEY, lat, lon).enqueue(object: Callback<WeatherDTO> {
            override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                if (response.isSuccessful && response.body()!= null) {
                    callback.onResponse(response.body()!!, clickCity(lat, lon).name)
                } else {
                    callback.error(IOException(MESSAGE_ERROR))
                }
            }

            override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                callback.error(t as IOException)
            }

        })
    }
}