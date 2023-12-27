package com.example.mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


class BlueFragment : Fragment() {
    var textView: TextView? = null
    var title: String? = null

    companion object {
        const val TITLE = "BlueFragment title"
        fun newInstance(title: String) =
            BlueFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                }
            }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(TITLE)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_blue, container, false)
        textView = view.findViewById(R.id.tvBlueFragmentTitle)
        textView?.text = title ?: "Please assign a title"
        return view
    }

}