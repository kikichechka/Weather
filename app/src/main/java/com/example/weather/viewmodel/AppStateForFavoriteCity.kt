package com.example.weather.viewmodel

import com.example.weather.model.Weather


sealed class AppStateForFavoriteCity {
    data class Luck(val weather: Weather): AppStateForFavoriteCity()
    data class Error(val error: Throwable): AppStateForFavoriteCity()
    object Loading: AppStateForFavoriteCity()
}
