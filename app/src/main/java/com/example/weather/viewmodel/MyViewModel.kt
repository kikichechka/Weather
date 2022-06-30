package com.example.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.RepoImpl
import java.lang.Thread.sleep

class MyViewModel(
    private val favoriteCityLiveData: MutableLiveData<AppStateForFavoriteCity> = MutableLiveData<AppStateForFavoriteCity>(),
    private val listCitiesLiveData: MutableLiveData<AppStateForListCities> = MutableLiveData<AppStateForListCities>(),
    private val repo: RepoImpl = RepoImpl()
) : ViewModel() {

    fun getFavoriteCityLiveData() = favoriteCityLiveData

    fun updateFavoriteCityLiveData() {
        favoriteCityLiveData.value = AppStateForFavoriteCity.Loading
        Thread {
            sleep(3000L)
            favoriteCityLiveData.postValue(AppStateForFavoriteCity.Luck(repo.getWeather()))
        }.start()

    }


    fun getListCitiesLiveData() = listCitiesLiveData
    fun getRussianCities() = getListWeather(true)
    fun getWorldCities() = getListWeather(false)

    private fun getListWeather(russian: Boolean) {
        Thread {
            val list = if (russian) repo.getRussianCitiesList() else repo.getWorldCitiesList()
            listCitiesLiveData.postValue(AppStateForListCities.Luck(list))
        }.start()
    }

}