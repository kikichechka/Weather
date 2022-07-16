package com.example.weather.utils

import java.io.BufferedReader
import java.util.stream.Collector
import java.util.stream.Collectors

const val KEY_BUNDLE_CITY_WEATHER = "bundle"
const val KEY_API = "X-Yandex-API-Key"
const val KEY_VALUE = "32d47294-c18c-4820-8829-bcd2d702245d"

fun getLines(reader: BufferedReader): String {
    return reader.lines().collect(Collectors.joining("\n"))
}