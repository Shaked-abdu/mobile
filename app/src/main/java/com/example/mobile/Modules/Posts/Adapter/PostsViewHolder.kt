package com.example.mobile.Modules.Posts.Adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.Model.Model
import com.example.mobile.Model.Post
import com.example.mobile.Modules.Posts.PostsFragmentDirections
import com.example.mobile.Modules.Posts.PostsRecyclerViewActivity
import com.example.mobile.R
import com.example.mobile.base.MyApplication
import com.squareup.picasso.Picasso

class PostsViewHolder(
    val itemView: View,
    listener: PostsRecyclerViewActivity.OnItemClickListener?,
    posts: List<Post>?
) :
    RecyclerView.ViewHolder(itemView) {
    var ownerTextView: TextView? = null
    var titleTextView: TextView? = null
    var descriptionTextView: TextView? = null
    var imageView: ImageView? = null
    var imageProgressBar: ProgressBar? = null
    var post: Post? = null
    var userId: String? = null
    var editImageView: ImageView? = null
    var deleteImageView: ImageView? = null


    init {
        titleTextView = itemView.findViewById(R.id.postTitle)
        descriptionTextView = itemView.findViewById(R.id.postDescription)
        ownerTextView = itemView.findViewById(R.id.postOwner)
        imageView = itemView.findViewById(R.id.postImage)
        imageProgressBar = itemView.findViewById(R.id.postImageProgressBar)
        editImageView = itemView.findViewById(R.id.ivEditPost)
        deleteImageView = itemView.findViewById(R.id.ivDeletePost)
        itemView.setOnClickListener {
            Log.i("TAG", "PostsViewHolder: position clicked: $adapterPosition")

            listener?.onItemClick(posts?.get(adapterPosition), adapterPosition)
            listener?.onPostClicked(post)
        }
        userId = signedInUserUid()

        editImageView!!.setOnClickListener(::edit)
        deleteImageView!!.setOnClickListener(::delete)


    }

    fun bind(post: Post?) {
        this.post = post
        titleTextView?.text = post?.title
        descriptionTextView?.text = post?.description
        Model.instance.getUserById(post?.owner!!) {
            ownerTextView?.text = it?.firstName + " " + it?.lastName
        }
        if (post?.imageUri != "") {
            imageProgressBar?.visibility = View.VISIBLE
            Picasso.get()
                .load(post?.imageUri)
                .into(imageView, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        imageProgressBar?.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        imageProgressBar?.visibility = View.GONE
                    }
                })
        }

        Log.i("OWNER", "User: $userId Post: ${post}")
        if (post.owner == userId) {
            Log.i("OWNER", "User is owner of post ${post.uid}")
            editImageView?.visibility = View.VISIBLE
            deleteImageView?.visibility = View.VISIBLE
        }else
        {
            editImageView?.visibility = View.GONE
            deleteImageView?.visibility = View.GONE
        }
    }

    fun signedInUserUid(): String {
        return MyApplication.Globals.appContext
            ?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            ?.getString("userUid", null) ?: ""
    }

    fun edit(view: View) {
        val action = PostsFragmentDirections.actionPostsFragmentToEditPostFragment(post?.uid!!)
        Navigation.findNavController(view).navigate(action)
    }

    fun delete(view: View) {
        Model.instance.deletePost(post!!) {
            Log.i("TAG", "Post deleted")
        }
    }
}
