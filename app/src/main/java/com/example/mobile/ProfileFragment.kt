package com.example.mobile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobile.Modules.AddPost.AddPostFragmentArgs
import com.example.mobile.databinding.FragmentPostsBinding
import com.example.mobile.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    var email: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root


        val email = arguments?.let { AddPostFragmentArgs.fromBundle(it).UID }

        Log.i("TAG", "ProfileFragment: $email")


        val emailTextView = binding.tvEmail
        emailTextView.text = email





        return view
    }
}