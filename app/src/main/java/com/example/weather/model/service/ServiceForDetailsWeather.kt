package com.example.weather.model.service

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weather.model.Weather
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.utils.*
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ServiceForDetailsWeather: IntentService("") {
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            it.getParcelableExtra<Weather>(BUNDLE_WEATHER_KEY)?.let { weather ->
                val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${weather.city.lat}&lon=${weather.city.lon}")
                val myConnection = uri.openConnection() as HttpsURLConnection
                myConnection.addRequestProperty(KEY_API, KEY_VALUE)

                Thread {
                    try {
                        val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                        val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)

                        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent().apply {
                            putExtra(KEY_WEATHER_DTO_FOR_SERVICE_FOR_DETAILS, weatherDTO)
                            action = KEY_FOR_SERVICE_FOR_DETAILS
                        })
                    } catch (e: Exception) {

                    } finally {
                        myConnection.disconnect()
                    }


                }.start()
            }
        }
    }
}