package com.example.weather.viewmodel

import com.example.weather.model.dto.WeatherDTO


sealed class AppStateForCity {
    data class Luck(val weather: WeatherDTO, val cityName: String): AppStateForCity()
    data class Error(val error: Throwable): AppStateForCity()
    object Loading: AppStateForCity()
}
