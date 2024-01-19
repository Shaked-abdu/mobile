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
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.mobile.BlueFragmentArgs
import com.example.mobile.Model.Model
import com.example.mobile.Model.Post
import com.example.mobile.R


class AddPostFragment : Fragment() {
    private var headerTextField: EditText? = null
    private var descriptionTextField: EditText? = null
    private var saveButton: Button? = null
    private var cancelButton: Button? = null
    private var email: String? = null

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
//        email = arguments?.let { AddPostFragmentArgs.fromBundle(it).EMAIL }
        email = arguments?.getString("EMAIL")

        setupUI(view)
        return view
    }

    private fun setupUI(view: View) {
        headerTextField = view.findViewById(R.id.etHeader)
        descriptionTextField = view.findViewById(R.id.etDescription)
        saveButton = view.findViewById(R.id.btnSavePost)
        cancelButton = view.findViewById(R.id.btnCancelPost)

        cancelButton?.setOnClickListener {
            Navigation.findNavController(it).popBackStack(R.id.postsFragment, false)
        }

        saveButton?.setOnClickListener {
            val header = headerTextField?.text.toString()
            val description = descriptionTextField?.text.toString()
            if (header.isEmpty() || description.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val post = Post(header, description, email?:"")
            Model.instance.addPost(post) {
                Toast.makeText(context, "Post saved successfully", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(it).popBackStack(R.id.postsFragment, false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }


}