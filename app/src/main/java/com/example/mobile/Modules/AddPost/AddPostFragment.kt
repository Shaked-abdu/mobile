package com.example.mobile.Modules.AddPost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.Navigation
import com.example.mobile.R


class AddPostFragment : Fragment() {
    private var descriptionTextField: EditText? = null
    private var saveButton: Button? = null
    private var cancelButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_post, container, false)
        setupUI(view)
        return view
    }

    private fun setupUI(view: View) {
        descriptionTextField = view.findViewById(R.id.etDescription)
        saveButton = view.findViewById(R.id.btnSavePost)
        cancelButton = view.findViewById(R.id.btnCancelPost)

        cancelButton?.setOnClickListener {
        Navigation.findNavController(it).popBackStack(R.id.postsFragment, false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }


}