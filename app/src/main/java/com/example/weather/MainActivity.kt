package com.example.weather

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.activity_main_container, DetailsWeatherFragment.newInstance())
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
}