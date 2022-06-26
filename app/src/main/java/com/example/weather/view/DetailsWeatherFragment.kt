package com.example.weather.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.weather.databinding.FragmentDetailsWeatherBinding
import com.example.weather.viewmodel.AppStateForFavoriteCity
import com.example.weather.viewmodel.MyViewModel
import com.google.android.material.snackbar.Snackbar


class DetailsWeatherFragment : Fragment() {
    private lateinit var binding: FragmentDetailsWeatherBinding
    private lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = DetailsWeatherFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        viewModel.getFavoriteCityLiveData().observe(viewLifecycleOwner,
            { AppStateForFavoriteCity -> showWeather(AppStateForFavoriteCity) }
        )
        viewModel.updateFavoriteCityLiveData()

    }

    private fun showWeather(data: AppStateForFavoriteCity) {
        val equal = "+"
        when (data) {
            is AppStateForFavoriteCity.Error -> {
                binding.progressDetails.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Не удалось загрузить данные", Toast.LENGTH_LONG).show()
            }
            AppStateForFavoriteCity.Loading -> {
                binding.progressDetails.visibility = View.VISIBLE
            }
            is AppStateForFavoriteCity.Ok -> {
                binding.progressDetails.visibility = View.GONE
                binding.cityNameDetails.text = data.weather.city.nameCity
                binding.cityCoordinatesDetails.text =
                    "${data.weather.city.lat} ${data.weather.city.lon}"
                if (data.weather.temperature > 0) {
                    binding.temperatureValue.text = "$equal ${data.weather.temperature}"
                } else {
                    binding.temperatureValue.text = "${data.weather.temperature}"
                }

                if (data.weather.feelsLike > 0) {
                    binding.feelsLikeValue.text = "$equal ${data.weather.feelsLike}"
                } else {
                    binding.feelsLikeValue.text = "${data.weather.feelsLike}"
                }
            }
        }
    }
}