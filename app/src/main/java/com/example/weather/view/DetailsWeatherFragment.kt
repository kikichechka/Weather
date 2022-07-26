package com.example.weather.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weather.R
import com.example.weather.databinding.FragmentDetailsWeatherBinding
import com.example.weather.model.Weather
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.model.service.ServiceForDetailsWeather
import com.example.weather.utils.*
import com.example.weather.viewmodel.AppStateForFavoriteCity
import com.example.weather.viewmodel.DetailsViewModel


class DetailsWeatherFragment : Fragment() {
    private var _binding: FragmentDetailsWeatherBinding? = null
    private val binding: FragmentDetailsWeatherBinding
        get() {
            return _binding!!
        }
    private lateinit var viewModel: DetailsViewModel
    private lateinit var receiver: BroadcastReceiver


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsWeatherBinding.inflate(inflater, container, false)
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

                receiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        intent?.let {
                            it.getParcelableExtra<WeatherDTO>(
                                KEY_WEATHER_DTO_FOR_SERVICE_FOR_DETAILS
                            )?.let { weatherDTO ->
                                binding.run {
                                    progressDetails.visibility = View.GONE
                                    cityNameDetails.text = data.weather.city.name
                                    cityCoordinatesDetails.text =
                                        "${data.weather.city.lat} ${data.weather.city.lon}"
                                    if (data.weather.temperature > 0) {
                                        temperatureValue.text = "$equal ${weatherDTO.fact.temp}"
                                    } else {
                                        temperatureValue.text = "${weatherDTO.fact.temp}"
                                    }

                                    if (data.weather.feelsLike > 0) {
                                        feelsLikeValue.text =
                                            "$equal ${weatherDTO.fact.feelsLike}"
                                    } else {
                                        feelsLikeValue.text = "${weatherDTO.fact.feelsLike}"
                                    }
                                }

                            }
                        }
                    }
                }
                LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
                    receiver, IntentFilter(KEY_FOR_SERVICE_FOR_DETAILS)
                )

                requireActivity().startService(
                    Intent(
                        requireContext(),
                        ServiceForDetailsWeather::class.java
                    ).apply {
                        putExtra(BUNDLE_WEATHER_KEY, data.weather)
                    })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }
}