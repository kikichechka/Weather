package com.example.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentDetailsWeatherBinding
import com.example.weather.model.Weather
import com.example.weather.utils.KEY_BUNDLE_CITY_WEATHER
import com.example.weather.viewmodel.AppStateForFavoriteCity
import com.example.weather.viewmodel.MyViewModel


class DetailsWeatherFragment : Fragment() {
    private lateinit var binding: FragmentDetailsWeatherBinding
    private lateinit var viewModel: MyViewModel

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
            viewModel = ViewModelProvider(this).get(MyViewModel::class.java).apply {
                getFavoriteCityLiveData().observe(viewLifecycleOwner,
                    { AppStateForFavoriteCity -> showWeatherFromLiveData(AppStateForFavoriteCity) })
                updateFavoriteCityLiveData()
            }
            binding.replayButton.setOnClickListener { viewModel.updateFavoriteCityLiveData() }
        } else {
            val weather = requireArguments().getParcelable<Weather>(KEY_BUNDLE_CITY_WEATHER)!!
            showWeatherFromBundle(weather)
            binding.replayButton.setOnClickListener {
                showWeatherFromBundle(weather)
            }
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
                binding.run {
                    progressDetails.visibility = View.GONE
                    cityNameDetails.text = data.weather.city.nameCity
                    cityCoordinatesDetails.text =
                        "${data.weather.city.lat} ${data.weather.city.lon}"
                    if (data.weather.temperature > 0) {
                        temperatureValue.text = "$equal ${data.weather.temperature}"
                    } else {
                        temperatureValue.text = "${data.weather.temperature}"
                    }

                    if (data.weather.feelsLike > 0) {
                        feelsLikeValue.text = "$equal ${data.weather.feelsLike}"
                    } else {
                        feelsLikeValue.text = "${data.weather.feelsLike}"
                    }
                }

            }
        }
    }

    private fun showWeatherFromBundle(weather: Weather) {
        val equal = '+'
        binding.apply {
            progressDetails.visibility = View.VISIBLE
            progressDetails.visibility = View.GONE
            cityNameDetails.text = weather.city.nameCity
            cityCoordinatesDetails.text =
                "${weather.city.lat}, ${weather.city.lon}"
            temperatureValue.text =
                if (weather.temperature > 0) "$equal ${weather.temperature}"
                else weather.temperature.toString()
            feelsLikeValue.text =
                if (weather.feelsLike > 0) "$equal ${weather.feelsLike}"
                else weather.feelsLike.toString()

        }


    }
}