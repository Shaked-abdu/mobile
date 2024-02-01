    package com.example.mobile

    import android.os.Bundle
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import com.example.mobile.databinding.FragmentProfileBinding
    import com.example.mobile.databinding.FragmentWeatherBinding

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private const val ARG_PARAM1 = "param1"
    private const val ARG_PARAM2 = "param2"

    /**
     * A simple [Fragment] subclass.
     * Use the [WeatherFragment.newInstance] factory method to
     * create an instance of this fragment.
     */
    class WeatherFragment : Fragment() {
        private var _binding: FragmentWeatherBinding? = null
        private val binding get() = _binding!!

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

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = FragmentWeatherBinding.inflate(inflater, container, false)
            val view = binding.root



            return view
        }

        private fun loadWeatherData() {

        }

    }