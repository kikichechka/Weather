package com.example.weather.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weather.databinding.FragmentListCitiesBinding


class ListCitiesFragment : Fragment() {
    private var _binding: FragmentListCitiesBinding? = null
    private val binding: FragmentListCitiesBinding
        get() {
            return _binding!!
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListCitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListCitiesFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}