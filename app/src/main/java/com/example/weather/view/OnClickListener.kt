package com.example.weather.view

import com.example.weather.model.Weather

interface OnClickListener {
    fun onItemClick(weather: Weather)
}