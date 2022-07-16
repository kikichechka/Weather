package com.example.weather.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.R
import com.example.weather.databinding.FragmentListCitiesBinding
import com.example.weather.model.Weather
import com.example.weather.utils.KEY_BUNDLE_CITY_WEATHER
import com.example.weather.viewmodel.AppStateForListCities
import com.example.weather.viewmodel.ListViewModel


class ListCitiesFragment : Fragment(), OnClickListener {
    private lateinit var viewModel: ListViewModel
    private val adapter = ListCitiesNameAdapter(this)
    private var isRussian = true
    private var _binding: FragmentListCitiesBinding? = null
    private val binding: FragmentListCitiesBinding
        get() {
            return _binding!!
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListCitiesFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java).also {
            it.getListCitiesLiveData().observe(viewLifecycleOwner,
                { AppStateForListCities -> showListCitiesName(AppStateForListCities) })
        }
        spotList(isRussian)

        with(binding) {
            recyclerviewListCities.adapter = adapter
            recyclerviewListCities.layoutManager = LinearLayoutManager(requireContext())

            buttonRussianListCities.setOnClickListener {
                isRussian = true
                spotList(isRussian)
            }
            buttonWorldListCities.setOnClickListener {
                isRussian = false
                spotList(isRussian)
            }
        }

    }

    private fun spotList(russian: Boolean) {
        if (russian) {
            binding.buttonRussianListCities.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            )
            binding.buttonWorldListCities.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            viewModel.getRussianCities()

        } else {
            binding.buttonRussianListCities.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding.buttonWorldListCities.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            )
            viewModel.getWorldCities()
        }
    }

    private fun showListCitiesName(data: AppStateForListCities) {
        when (data) {
            is AppStateForListCities.Luck -> {
                adapter.setList(data.listCities)
            }
            is AppStateForListCities.Error -> {

            }
            is AppStateForListCities.Loading -> {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(weather: Weather) {
        val bundle = Bundle()
        bundle.putParcelable(KEY_BUNDLE_CITY_WEATHER, weather)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_main_container, DetailsWeatherFragment.newInstance(bundle))
            .commit()
    }
}
