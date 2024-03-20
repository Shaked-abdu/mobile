package com.example.mobile.Modules.Posts.Adapter

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Surface
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AlertDialogLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.Model.Model
import com.example.mobile.Model.Post
import com.example.mobile.Modules.Posts.PostsFragmentDirections
import com.example.mobile.Modules.Posts.PostsRecyclerViewActivity
import com.example.mobile.R
import com.example.mobile.base.MyApplication
import com.squareup.picasso.Picasso
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier


import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.navigation.PopUpToBuilder
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.annotations.concurrent.Background
import io.ktor.http.cio.websocket.Frame
import io.ktor.utils.io.makeShared
import androidx.compose.ui.graphics.Color.Companion.Red as Red1


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
        titleTextView = itemView.findViewById(R.id.tv_postTitle)
        descriptionTextView = itemView.findViewById(R.id.postDescription)
        ownerTextView = itemView.findViewById(R.id.tv_postOwner)
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
        } else {
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


    private fun delete(view: View) {

        MaterialAlertDialogBuilder(view.context)
                    .setTitle("Delete Post")
                    .setMessage("Are you sure?")
                    .setNegativeButton("NO") { dialog, which ->
                        Navigation.findNavController(view).popBackStack()
                    }
                    .setPositiveButton("YES") { dialog, which ->
                        showSnackBar("Post Deleted", view)
                        Model.instance.deletePost(post!!) {
                            Log.i("TAG", "Post deleted")
                            Model.instance.refreshAllPosts()
                        }
                    }.show()
    }

    private fun showSnackBar(msg:String, view: View){
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show()
    }
}

