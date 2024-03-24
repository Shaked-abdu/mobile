package com.example.mobile.Modules.Posts

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.example.mobile.Modules.Posts.PostClickedFragment
import com.example.mobile.Model.Model
import com.example.mobile.Model.Post
import com.example.mobile.Model.StorageModel
import com.example.mobile.Model.User
import com.example.mobile.R
import com.example.mobile.databinding.FragmentEditPostBinding
import com.example.mobile.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso


class EditPostFragment : Fragment() {
    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    var textViewTitle: TextView? = null
    var textViewDescription: TextView? = null
    var imageView: ImageView? = null
    var imageProgressBarEdit: ProgressBar? = null
    var postUid: String? = null
    var post: Post? = null
    private var imageUri: Uri? = null
    private val storageModel = StorageModel("post_images")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditPostBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        postUid = arguments?.let { EditPostFragmentArgs.fromBundle(it).POSTUID }

        val cancelButton: Button = binding.btnCancelPostEdit
        cancelButton.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        val saveButton: Button = binding.btnSavePostEdit
        saveButton.setOnClickListener(::save)

        textViewTitle = binding.etHeaderEdit
        textViewDescription = binding.etDescriptionEdit
        imageView = binding.ivEditPostUploadImage
        imageProgressBarEdit = binding.imageProgressBarEdit

        imageView!!.setOnClickListener(::uploadImage)


        Model.instance.getPostById(postUid!!) {
            post = it
            textViewTitle?.text = it?.title
            textViewDescription?.text = it?.description
            Picasso.get()
                .load(it?.imageUri)
                .into(imageView, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        imageProgressBarEdit?.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        imageProgressBarEdit?.visibility = View.GONE
                    }
                })
        }
        return view
    }

    fun save(view: View) {
        val title = binding.etHeaderEdit.text.toString()
        val description = binding.etDescriptionEdit.text.toString()

        if (title.equals(post!!.title) && description.equals(post!!.description) && imageUri == null) {

            Toast.makeText(
                this.context,
                "Nothing to update.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (title.equals(post!!.title).not() || description.equals(post!!.description)
                    .not()
            ) {
                post = Post(postUid!!, title, description, post!!.owner, post!!.imageUri)
                Model.instance.updatePostByUid(post!!) {
                    Log.i("TAG", "Report updated successfully.")
                }
            }
            if (imageUri != null) {
                imageUri?.let {
                    storageModel.uploadFile(it,
                        onComplete = { uri ->
                            post =
                                Post(post!!.uid, title, description, post!!.owner, uri)
                            Model.instance.updatePostByUid(post!!) {
                            }
                            imageUri = null
                        },
                        onFailure = { message ->
                            imageUri = null
                        }
                    )
                }
            }

            Navigation.findNavController(view).popBackStack()
        }
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
        binding.ivEditPostUploadImage.setImageURI(uri)
    }
}