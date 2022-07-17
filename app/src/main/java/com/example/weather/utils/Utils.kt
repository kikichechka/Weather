package com.example.weather.utils

import java.io.BufferedReader
import java.util.stream.Collectors

const val KEY_BUNDLE_CITY_WEATHER = "bundle"
const val KEY_API = "X-Yandex-API-Key"
const val KEY_VALUE = "32d47294-c18c-4820-8829-bcd2d702245d"
const val KEY_FOR_SERVICE_FOR_DETAILS = "wave"
const val BUNDLE_WEATHER_KEY = "wave1"
const val KEY_WEATHER_DTO_FOR_SERVICE_FOR_DETAILS = "wave1"

fun getLines(reader: BufferedReader): String {
    return reader.lines().collect(Collectors.joining("\n"))
}