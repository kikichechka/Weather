package com.example.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.model.repo.*
import com.example.weather.model.repo.retrofit.RepoDetailsWeatherRetrofitImpl
import com.example.weather.utils.MESSAGE_ERROR
import java.io.IOException
import java.lang.IllegalStateException

class DetailsViewModel(
    private val favoriteCityLiveData: MutableLiveData<AppStateForCity> = MutableLiveData<AppStateForCity>(),
    private val clickCityNameLiveData: MutableLiveData<AppStateForCity> = MutableLiveData<AppStateForCity>(),
    private val repoFavoriteCity: RepoFavoriteWeather = RepoFavoriteWeatherImpl()
) : ViewModel() {

    private lateinit var repoDetailsWeather: RepoDetailsWeather


    // загрузка избранного города
    fun getFavoriteCityLiveData() = favoriteCityLiveData

    fun updateFavoriteCityLiveData() { // Обновление избранного города
        choiceRepo()
        favoriteCityLiveData.value = AppStateForCity.Loading
        val data = repoFavoriteCity.getFavoriteWeather()
        repoDetailsWeather.getWeather(data.city.lat, data.city.lon, callbackForFavoriteCity)
    }

    private val callbackForFavoriteCity = object: MyCallback {
        override fun onResponse(weatherDTO: WeatherDTO, cityName: String) {
            favoriteCityLiveData.postValue(AppStateForCity.Luck(weatherDTO, cityName))
        }

        override fun error(e: IOException) {
            favoriteCityLiveData.postValue(AppStateForCity.Error(IllegalStateException(MESSAGE_ERROR)))
        }

    }

    //загрузка по клику
    fun getClickCityNameLiveData() = clickCityNameLiveData

    fun updateClickCityNameLiveData(lat: Double, lon: Double) { // Обновление города по клику
        choiceRepo()
        clickCityNameLiveData.value = AppStateForCity.Loading
        repoDetailsWeather.getWeather(lat, lon, callbackForClickCity)
    }

    private val callbackForClickCity = object: MyCallback {
        override fun onResponse(weatherDTO: WeatherDTO, cityName: String) {
            clickCityNameLiveData.postValue(AppStateForCity.Luck(weatherDTO, cityName))
        }

        override fun error(e: IOException) {
            clickCityNameLiveData.postValue(AppStateForCity.Error(IllegalStateException(MESSAGE_ERROR)))
        }

    }

    private fun choiceRepo() {
        repoDetailsWeather = when (3) {
            1 -> {
                RepoDetailsWeatherOkHttpImpl()
            }
            2 -> {
                RepoDetailsWeatherConnectionImpl()
            }
            else -> {
                RepoDetailsWeatherRetrofitImpl()
            }
        }
    }
}