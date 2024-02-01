package com.example.mobile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobile.base.MyApplication
import com.example.mobile.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    var userId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root


        userId = signedInUserUid()


        val emailTextView = binding.tvEmail
        emailTextView.text = userId!!


        return view
    }

    fun signedInUserUid(): String {
        return MyApplication.Globals.appContext
            ?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            ?.getString("userUid", null) ?: ""
    }
}