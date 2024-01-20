package com.example.mobile.Modules.AddPost

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.example.mobile.BlueFragmentArgs
import com.example.mobile.Model.Model
import com.example.mobile.Model.Post
import com.example.mobile.Model.StorageModel
import com.example.mobile.R
import com.example.mobile.databinding.FragmentAddPostBinding
import com.example.mobile.databinding.FragmentPostsBinding


class AddPostFragment : Fragment() {
    private var headerTextField: EditText? = null
    private var descriptionTextField: EditText? = null
    private var imageUploaded = false


    //    private var  ivUploadImage: ImageView? = null
    private var saveButton: Button? = null
    private var cancelButton: Button? = null
    private var email: String? = null
    private var imageUri: Uri? = null
    private var _binding: FragmentAddPostBinding? = null
    private val storageModel = StorageModel("posts_images")


    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)
        val view = binding.root
        val ivUploadImage = binding.ivAddPostUploadImage

        ivUploadImage.setOnClickListener(::uploadImage)
        setupUI(view)
        return view
    }

    private fun setupUI(view: View) {
        headerTextField = binding.etHeader
        descriptionTextField = binding.etDescription
//        ivUploadImage = binding.ivAddPostUploadImage
        saveButton = binding.btnSavePost
        cancelButton = binding.btnCancelPost

//        ivUploadImage?.setOnClickListener {
//            uploadImage(it)
//        }

        cancelButton?.setOnClickListener {
            Navigation.findNavController(it).popBackStack(R.id.postsFragment, false)
        }

        saveButton?.setOnClickListener {
            if (!imageUploaded) {
                Toast.makeText(
                    context,
                    "Please upload an image.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {
                val header = headerTextField?.text.toString()
                val description = descriptionTextField?.text.toString()
                if (header.isEmpty() || description.isEmpty()) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                storageModel.uploadFile(imageUri!!,
                    onComplete = { uri ->
                        savePost(header, description, uri)
                    },
                    onFailure = { message ->

                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun uploadImage(view: View) {
        resultLauncher.launch("image/*")
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
        binding.ivAddPostUploadImage.setImageURI(it)
        imageUploaded = true

    }

    private fun savePost(title: String, description: String, uri: String) {
        val post = Post(title, description, email ?: "", uri)
        Model.instance.addPost(post) {
            Toast.makeText(context, "Post saved successfully", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(binding.root)
                .popBackStack(R.id.postsFragment, false) // binding.root is the view
        }
    }

}