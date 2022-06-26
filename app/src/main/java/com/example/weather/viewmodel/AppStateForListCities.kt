package com.example.weather.viewmodel

sealed class AppStateForListCities {
    data class Ok(val listCities: List<Any>): AppStateForListCities()
    data class Error(val error: Throwable): AppStateForListCities()
    object Loading: AppStateForListCities()
}