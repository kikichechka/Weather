package com.example.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.weather.R
import com.example.weather.databinding.FragmentDetailsWeatherBinding
import com.example.weather.model.Weather
import com.example.weather.utils.*
import com.example.weather.viewmodel.AppStateForCity
import com.example.weather.viewmodel.DetailsViewModel
import com.squareup.picasso.Picasso


class DetailsWeatherFragment : Fragment() {
    private var _binding: FragmentDetailsWeatherBinding? = null
    private val binding: FragmentDetailsWeatherBinding
        get() {
            return _binding!!
        }
    private lateinit var viewModel: DetailsViewModel
    //private lateinit var receiver: BroadcastReceiver


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

        if (contentBundle == null) { // если первый запуск
            viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java).apply {
                getFavoriteCityLiveData().observe(viewLifecycleOwner,
                    { AppStateForFavoriteCity ->
                        showWeather(AppStateForFavoriteCity)
                    })
                updateFavoriteCityLiveData()
            }
            binding.replayButton.setOnClickListener {
                //LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
                viewModel.updateFavoriteCityLiveData()
            }

        } else {
            val weather = requireArguments().getParcelable<Weather>(KEY_BUNDLE_CITY_WEATHER)!!

            viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java).apply {
                getClickCityNameLiveData().observe(viewLifecycleOwner,
                    { AppStateForCity ->
                        showWeather(AppStateForCity)
                    })
                updateClickCityNameLiveData(weather.city.lat, weather.city.lon)
            }

            binding.replayButton.setOnClickListener {
                //LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
                viewModel.updateClickCityNameLiveData(weather.city.lat, weather.city.lon)
            }
        }
    }


    private fun showWeather(data: AppStateForCity) {
        when (data) {
            is AppStateForCity.Error -> {
                binding.progressDetails.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    resources.getText(R.string.error),
                    Toast.LENGTH_LONG
                ).show()
            }
            AppStateForCity.Loading -> {
                binding.progressDetails.visibility = View.VISIBLE
            }
            is AppStateForCity.Luck -> {
                binding.run {
                    val equal = "+"
                    progressDetails.visibility = View.GONE
                    cityNameDetails.text = data.cityName
                    cityCoordinatesDetails.text =
                        "${data.weather.info.lat} ${data.weather.info.lon}"

                    if (data.weather.fact.temp > 0) {
                        temperatureValue.text = "$equal ${data.weather.fact.temp}"
                    } else {
                        temperatureValue.text = "${data.weather.fact.temp}"
                    }

                    if (data.weather.fact.feelsLike > 0) {
                        feelsLikeValue.text =
                            "$equal ${data.weather.fact.feelsLike}"
                    } else {
                        feelsLikeValue.text = "${data.weather.fact.feelsLike}"
                    }

                    val condition = data.weather.fact.condition
                    stateWeather.let {
                        if (condition.equals("clear")) it.text = "Ясно"
                        if (condition.equals("partly-cloudy")) it.text = "Малооблачно"
                        if (condition.equals("cloudy")) it.text = "Облачно с прояснениями"
                        if (condition.equals("overcast")) it.text = "Пасмурно"
                        if (condition.equals("drizzle")) it.text = "Морось"
                        if (condition.equals("light-rain")) it.text = "Небольшой дождь"
                        if (condition.equals("rain")) it.text = "Дождь"
                        if (condition.equals("showers")) it.text = "Ливень"
                        if (condition.equals("heavy-rain")) it.text = "Сильный дождь"
                        if (condition.equals("continuous-heavy-rain")) it.text =
                            "Длительный сильный дождь"
                        if (condition.equals("wet-snow")) it.text = "Дождь со снегом"
                        if (condition.equals("moderate-rain")) it.text = "Умеренно сильный дождь"
                        if (condition.equals("showers")) it.text = "Ливень"
                        if (condition.equals("snow")) it.text = "Снег"
                        if (condition.equals("light-snow")) it.text = "Небольшой снег"
                        if (condition.equals("snow-showers")) it.text = "Снегопад"
                        if (condition.equals("hail")) it.text = "Град"
                        if (condition.equals("thunderstorm")) it.text = "Гроза"
                        if (condition.equals("thunderstorm-with-rain")) it.text = "Дождь с грозой"
                        if (condition.equals("thunderstorm-with-hail")) it.text = "Гроза с градом"

                    }

                    stateWeatherIcon.loadUrl("https://yastatic.net/weather/i/icons/funky/dark/${data.weather.fact.icon}.svg.")
                    Picasso.get().load("https://yastatic.net/weather/i/icons/funky/dark/${data.weather.fact.icon}.svg.").into(stateWeatherIcon)
                }
                //stateWeatherIcon.load("https://yastatic.net/weather/i/icons/funky/dark/${data.weather.fact.icon}.svg.")
                //Glide.with(this.stateWeatherIcon).load("https://yastatic.net/weather/i/icons/funky/dark/${data.weather.fact.icon}.svg.").into(stateWeatherIcon)
            }
            //showWeatherThroughReceiver(data.weather)
        }
    }

    private fun ImageView.loadUrl(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry {add (SvgDecoder(this@loadUrl.context))}
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }


/*private fun getDataThroughReceiver(weather: Weather) {
    receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                it.getParcelableExtra<WeatherDTO>(KEY_WEATHER_DTO_FOR_SERVICE_FOR_DETAILS)
                    ?.let { weatherDTO ->
                        binding.run {
                            displayInfo(weather, weatherDTO)
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
            putExtra(BUNDLE_WEATHER_KEY, weather)
        })
}*/


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        //LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }
}