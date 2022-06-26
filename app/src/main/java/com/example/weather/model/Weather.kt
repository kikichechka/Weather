package com.example.weather.model

data class Weather(
    val city: City,
    val temperature: Int,
    val feelsLike: Int,
    var like: Boolean
)

data class City(
    val nameCity: String,
    val lat: Double,
    val lon: Double
)

fun defaultCity() = Weather(City("Воронеж", 51.6755, 39.20888), 29, 27, true)

