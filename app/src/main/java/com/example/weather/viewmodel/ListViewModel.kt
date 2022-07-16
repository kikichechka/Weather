package com.example.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.RepoImpl

class ListViewModel(
    private val listCitiesLiveData: MutableLiveData<AppStateForListCities> = MutableLiveData<AppStateForListCities>(),
    private val repo: RepoImpl = RepoImpl()
) : ViewModel(){


    fun getListCitiesLiveData() = listCitiesLiveData
    fun getRussianCities() = getListWeather(true)
    fun getWorldCities() = getListWeather(false)

    private fun getListWeather(russian: Boolean) {
        Thread {
            val list = if (russian) repo.getRussianCitiesListFromLocal() else repo.getWorldCitiesListFromLocal()
            listCitiesLiveData.postValue(AppStateForListCities.Luck(list))
        }.start()
    }

}