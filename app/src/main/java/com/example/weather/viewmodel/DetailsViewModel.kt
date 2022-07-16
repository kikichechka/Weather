package com.example.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.RepoImpl

class DetailsViewModel(
    private val favoriteCityLiveData: MutableLiveData<AppStateForFavoriteCity> = MutableLiveData<AppStateForFavoriteCity>(),
    private val clickCityNameLiveData: MutableLiveData<AppStateForFavoriteCity> = MutableLiveData<AppStateForFavoriteCity>(),
    private val repo: RepoImpl = RepoImpl()
) : ViewModel(){

    fun getFavoriteCityLiveData() = favoriteCityLiveData
    fun updateFavoriteCityLiveData() {

        favoriteCityLiveData.value = AppStateForFavoriteCity.Loading
        Thread {
            favoriteCityLiveData.postValue(AppStateForFavoriteCity.Luck(repo.getFavoriteWeather()))
        }.start()
    }


    fun getClickCityNameLiveData() = clickCityNameLiveData
    fun updateClickCityNameLiveData(data: String) {
        clickCityNameLiveData.value = AppStateForFavoriteCity.Loading
        Thread {
            clickCityNameLiveData.postValue(AppStateForFavoriteCity.Luck(repo.getClickWeather(data)))
        }.start()
    }
}