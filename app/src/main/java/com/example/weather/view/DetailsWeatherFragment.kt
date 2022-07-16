package com.example.weather.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentDetailsWeatherBinding
import com.example.weather.model.Weather
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.utils.KEY_API
import com.example.weather.utils.KEY_BUNDLE_CITY_WEATHER
import com.example.weather.utils.KEY_VALUE
import com.example.weather.utils.getLines
import com.example.weather.viewmodel.AppStateForFavoriteCity
import com.example.weather.viewmodel.DetailsViewModel
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection as HttpsURLConnection


class DetailsWeatherFragment : Fragment() {
    private lateinit var binding: FragmentDetailsWeatherBinding
    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsWeatherFragment {
            val fragment = DetailsWeatherFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contentBundle = requireArguments().getParcelable<Weather>(KEY_BUNDLE_CITY_WEATHER)

        if (contentBundle == null) {
            viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java).apply {
                getFavoriteCityLiveData().observe(viewLifecycleOwner,
                    { AppStateForFavoriteCity ->
                        showWeatherFromLiveData(AppStateForFavoriteCity)
                    })
                updateFavoriteCityLiveData()
            }
            binding.replayButton.setOnClickListener { viewModel.updateFavoriteCityLiveData() }

        } else {
            val weather = requireArguments().getParcelable<Weather>(KEY_BUNDLE_CITY_WEATHER)!!

            viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java).apply {
                getClickCityNameLiveData().observe(viewLifecycleOwner,
                    { AppStateForFavoriteCity ->
                        showWeatherFromLiveData(AppStateForFavoriteCity)
                    })
                updateClickCityNameLiveData(weather.city.name)
            }

            binding.replayButton.setOnClickListener { viewModel.updateClickCityNameLiveData(weather.city.name) }
        }
    }


    private fun showWeatherFromLiveData(data: AppStateForFavoriteCity) {
        val equal = "+"
        when (data) {
            is AppStateForFavoriteCity.Error -> {
                binding.progressDetails.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    resources.getText(R.string.error),
                    Toast.LENGTH_LONG
                ).show()
            }
            AppStateForFavoriteCity.Loading -> {
                binding.progressDetails.visibility = View.VISIBLE
            }
            is AppStateForFavoriteCity.Luck -> {

                var newData = data
                val handler = Handler(Looper.getMainLooper())
                val uri =
                    URL("https://api.weather.yandex.ru/v2/informers?lat=${newData.weather.city.lat}&lon=${newData.weather.city.lon}")
                val myConnection = uri.openConnection() as HttpsURLConnection
                myConnection.addRequestProperty(KEY_API, KEY_VALUE)

                Thread {
                    val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                    val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)

                    newData.weather.apply {
                        feelsLike = weatherDTO.fact.feelsLike
                        temperature = weatherDTO.fact.temp
                    }

                    handler.post {
                        binding.run {
                            progressDetails.visibility = View.GONE
                            cityNameDetails.text = newData.weather.city.name
                            cityCoordinatesDetails.text =
                                "${newData.weather.city.lat} ${newData.weather.city.lon}"
                            if (newData.weather.temperature > 0) {
                                temperatureValue.text = "$equal ${newData.weather.temperature}"
                            } else {
                                temperatureValue.text = "${newData.weather.temperature}"
                            }

                            if (newData.weather.feelsLike > 0) {
                                feelsLikeValue.text = "$equal ${newData.weather.feelsLike}"
                            } else {
                                feelsLikeValue.text = "${newData.weather.feelsLike}"
                            }
                        }
                    }
                }.start()
            }
        }
    }
}