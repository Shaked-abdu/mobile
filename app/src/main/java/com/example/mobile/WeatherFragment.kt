package com.example.mobile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import com.example.mobile.databinding.FragmentProfileBinding
import com.example.mobile.databinding.FragmentWeatherBinding
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.get
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Daily(
    val time: List<String>,
    val wave_height_max: List<Double>,
    val wave_direction_dominant: List<Double>,
    val wave_period_max: List<Double>
)

@Serializable
data class Response(
    val daily: Daily
)

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private var httpClient: HttpClient? = null
    private var snapperWeather: Response? = null
    private var currumbinWeather: Response? = null
    private var brunswickWeather: Response? = null
    private var wategosWeather: Response? = null
    private var location: String? = null
    private var locations = arrayOf("Snapper Rocks", "Currumbin", "Brunswick", "Wategos")
    private var autoComplete: AutoCompleteTextView? = null


    companion object {
        const val snapperRocksEndpoint =
            "https://marine-api.open-meteo.com/v1/marine?latitude=-28.161920&longitude=153.548920&daily=wave_height_max,wave_direction_dominant,wave_period_max"
        const val currumbinEndpoint =
            "https://marine-api.open-meteo.com/v1/marine?latitude=-28.124822&longitude=153.485386&daily=wave_height_max,wave_direction_dominant,wave_period_max"
        const val brunswickEndpoint =
            "https://marine-api.open-meteo.com/v1/marine?latitude=-28.534937&longitude=153.556774&daily=wave_height_max,wave_direction_dominant,wave_period_max"
        const val wategosEndpoint =
            "https://marine-api.open-meteo.com/v1/marine?latitude=-28.634552&longitude=153.632548&daily=wave_height_max,wave_direction_dominant,wave_period_max"
    }


    override fun onResume() {
        super.onResume()
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_location, locations)
        binding.actvChangeLocation.setAdapter(arrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val view = binding.root
        autoComplete = binding.actvChangeLocation

        location = autoComplete?.text.toString()
        binding.actvChangeLocation.setOnItemClickListener { parent, view, position, id ->
            location = parent.getItemAtPosition(position).toString()
            changeLocation(view)
        }



        httpClient = HttpClient(Android) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
        loadWeatherData()
        changeLocation(view)
        httpClient?.close()

        return view
    }

    private fun loadWeatherData() {
        try {
            runBlocking {
                snapperWeather = httpClient?.get<Response>(snapperRocksEndpoint)
                currumbinWeather = httpClient?.get<Response>(currumbinEndpoint)
                brunswickWeather = httpClient?.get<Response>(brunswickEndpoint)
                wategosWeather = httpClient?.get<Response>(wategosEndpoint)

            }
        } catch (e: Exception) {
            Log.e("WeatherFragment", "Error: $e")
        }
    }

    private fun changeLocation(view: View) {
        when (location) {

            "Snapper Rocks" -> {
                putData(snapperWeather)
                binding.mainWeatherimageView.setImageResource(R.drawable.snapperrocks)
            }

            "Currumbin" -> {
                putData(currumbinWeather)
                binding.mainWeatherimageView.setImageResource(R.drawable.currumbin)
            }

            "Brunswick" -> {
                putData(brunswickWeather)
                binding.mainWeatherimageView.setImageResource(R.drawable.brunswick)
            }

            "Wategos" -> {
                putData(wategosWeather)
                binding.mainWeatherimageView.setImageResource(R.drawable.wategos)
            }
        }

    }

    private fun putData(weatherData: Response?) {
        binding.dateTextView1.text = weatherData?.daily?.time?.get(0)
        binding.dateTextView2.text = weatherData?.daily?.time?.get(1)
        binding.dateTextView3.text = weatherData?.daily?.time?.get(2)

        binding.waveHeightTextView1.text =
            getString(R.string.wave_height, weatherData?.daily?.wave_height_max?.get(0).toString())
        binding.waveHeightTextView2.text =
            getString(R.string.wave_height, weatherData?.daily?.wave_height_max?.get(1).toString())
        binding.waveHeightTextView3.text =
            getString(R.string.wave_height, weatherData?.daily?.wave_height_max?.get(2).toString())

        binding.waveDirectionTextView1.text = getString(
            R.string.wave_direction,
            weatherData?.daily?.wave_direction_dominant?.get(0).toString()
        )
        binding.waveDirectionTextView2.text = getString(
            R.string.wave_direction,
            weatherData?.daily?.wave_direction_dominant?.get(1).toString()
        )
        binding.waveDirectionTextView3.text = getString(
            R.string.wave_direction,
            weatherData?.daily?.wave_direction_dominant?.get(2).toString()
        )

        binding.wavePeriodTextView1.text =
            getString(R.string.wave_period, weatherData?.daily?.wave_period_max?.get(0).toString())
        binding.wavePeriodTextView2.text =
            getString(R.string.wave_period, weatherData?.daily?.wave_period_max?.get(1).toString())
        binding.wavePeriodTextView3.text =
            getString(R.string.wave_period, weatherData?.daily?.wave_period_max?.get(2).toString())
    }
}