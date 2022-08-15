package com.example.weather.model.repo

import com.example.weather.model.favoriteCity

class RepoFavoriteWeatherImpl: RepoFavoriteWeather {
    override fun getFavoriteWeather() = favoriteCity()
}