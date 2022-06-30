package com.example.weather.viewmodel

import com.example.weather.model.Weather

sealed class AppStateForListCities {
    data class Luck(val listCities: List<Weather>): AppStateForListCities()
    data class Error(val error: Throwable): AppStateForListCities()
    object Loading: AppStateForListCities()
}