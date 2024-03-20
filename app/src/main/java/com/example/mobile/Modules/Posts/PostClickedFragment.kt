package com.example.mobile.Modules.Posts

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.mobile.Modules.Posts.PostClickedFragmentArgs
import com.example.mobile.Model.Model
import com.example.mobile.Model.Post
import com.example.mobile.Model.StorageModel
import com.example.mobile.R
import com.example.mobile.databinding.FragmentEditPostBinding
import com.example.mobile.databinding.FragmentPostClickedBinding
import com.squareup.picasso.Picasso
import io.ktor.util.valuesOf


open class PostClickedFragment : PostsFragment() {

    private var _binding: FragmentPostClickedBinding? = null
    private val binding get() = _binding!!

    var descriptionTextView: TextView? = null
    var titleTextView: TextView? = null
    var imageView1: ImageView? = null
    var postOwner: TextView?= null
    var postUid: String? = null
    var post: Post? = null
    var imageProgressBarEdit: ProgressBar? = null

    private val storageModel = StorageModel("post_images")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPostClickedBinding.inflate(inflater, container, false)
        val view = binding.root
        postUid = arguments?.let { PostClickedFragmentArgs.fromBundle(it).POSTUID }

        postOwner = binding.tvPostOwnerClicked
        titleTextView = binding.tvTitlePostClicked
        imageView1 = binding.ivPostImageClicked
        descriptionTextView = binding.tvPostClickedDescription



        Model.instance.getPostById(postUid!!) {
            post = it
            Model.instance.getUserById(post?.owner!!){
                postOwner?.text = it?.firstName +" "+ it?.lastName
            }
            //postOwner?.text =
            titleTextView?.text = it?.title
            descriptionTextView?.text = it?.description
            Picasso.get()
                .load(it?.imageUri)
                .into(imageView1 , object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        imageProgressBarEdit?.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        imageProgressBarEdit?.visibility = View.GONE
                    }
                })
        }

return  view

    }
}

/*class BlueFragment : Fragment() {

        Model.instance.getPostById(postOwner!!) {
            post = it
            titleTextView?.text = it?.title
            descriptionTextView?.text = it?.description
            Picasso.get()
                .load(it?.imageUri)
                .into(imageView1, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        imageProgressBarEdit?.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        imageProgressBarEdit?.visibility = View.GONE
                    }
                })
        }
*/