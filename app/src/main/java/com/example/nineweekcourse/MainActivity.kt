package com.example.nineweekcourse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

  private val forecastRepository = ForecastRepository()

//  region setup method

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val etZipcode: EditText = findViewById(R.id.etZipcode)
    val btnSubmit: Button = findViewById(R.id.btnSubmit)

    btnSubmit.setOnClickListener {
      val zipcode: String = etZipcode.text.toString()

      if (zipcode.length != 5) {
        Toast.makeText(this, R.string.zipcode_entry_error, Toast.LENGTH_SHORT).show()
      } else {
        forecastRepository.loadForecast(zipcode)
      }
    }

    val forecastList: RecyclerView = findViewById(R.id.rvForecastList)
    forecastList.layoutManager = LinearLayoutManager(this)

    val dailyForecastAdapter = DailyForecastAdapter() { forecastItem ->
      val msg =
        getString(R.string.forecast_clicked_format, forecastItem.temp, forecastItem.description)
      Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    forecastList.adapter = dailyForecastAdapter

    val weeklyForecastObserver = Observer<List<DailyForecast>> { forecastItems ->
      // update our list adapter
      dailyForecastAdapter.submitList(forecastItems)
    }

    forecastRepository.weeklyForecast.observe(this, weeklyForecastObserver)
  }
}