package com.example.weather

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.view.DetailsWeatherFragment
import com.example.weather.view.ListCitiesFragment
import com.example.weather.view.MapFragment

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() {
            return _binding!!
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val bundle = Bundle()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.activity_main_container, DetailsWeatherFragment.newInstance(bundle))
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_list_cities -> supportFragmentManager
                .beginTransaction()
                .add(R.id.activity_main_container, ListCitiesFragment.newInstance())
                .addToBackStack("list")
                .commit()
            R.id.menu_world_map -> supportFragmentManager
                .beginTransaction()
                .add(R.id.activity_main_container, MapFragment.newInstance())
                .addToBackStack("map")
                .commit()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}