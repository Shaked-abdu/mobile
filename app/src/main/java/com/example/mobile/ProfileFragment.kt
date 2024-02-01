package com.example.mobile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mobile.base.MyApplication
import com.example.mobile.databinding.FragmentProfileBinding
import com.example.mobile.Model.Model
import com.example.mobile.Model.StorageModel
import com.example.mobile.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {
    private val storageModel = StorageModel("profile_images")

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var userId: String? = null
    private var imageUri: Uri? = null
    private var user: User? = null
    var imageView: ImageView? = null
    var imageProgressBar: ProgressBar? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root


        userId = signedInUserUid()
        imageView = binding.ivProfile
        imageProgressBar = binding.profileProgressBar

        imageView!!.setOnClickListener(::uploadImage)
        Model.instance.getUserById(userId!!) {
            user = it
            Picasso.get()
                .load(it?.imageUri)
                .into(imageView, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        imageProgressBar?.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        imageProgressBar?.visibility = View.GONE
                    }
                })
        }




        return view
    }

    fun signedInUserUid(): String {
        return MyApplication.Globals.appContext
            ?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            ?.getString("userUid", null) ?: ""
    }

    private fun uploadImage(view: View) {
        resultLauncher.launch("image/*")
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            saveImage(it)

        }
    }

    fun saveImage(uri: Uri) {
        imageUri = uri
        binding.ivProfile.setImageURI(uri)
        storageModel.uploadFile(imageUri!!,
            onComplete = { uri ->
                user = User(userId!!, uri, user!!.firstName, user!!.lastName)
                Model.instance.updateUserById(user!!) {
                    Toast.makeText(
                        context,
                        "Image Uploaded Successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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