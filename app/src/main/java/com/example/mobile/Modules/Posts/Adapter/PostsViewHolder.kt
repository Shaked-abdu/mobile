package com.example.mobile.Modules.Posts.Adapter

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.Model.Post
import com.example.mobile.Modules.Posts.PostsRecyclerViewActivity
import com.example.mobile.R

class PostsViewHolder(
    val itemView: View,
    listener: PostsRecyclerViewActivity.OnItemClickListener?,
    posts: MutableList<Post>?
) :
    RecyclerView.ViewHolder(itemView) {

    var titleTextView: TextView? = null
    var descriptionTextView: TextView? = null
    var post: Post? = null

    init {
        titleTextView = itemView.findViewById(R.id.postTitle)
        descriptionTextView = itemView.findViewById(R.id.postDescription)
        itemView.setOnClickListener {
            Log.i("TAG", "PostsViewHolder: position clicked: $adapterPosition")

            listener?.onItemClick(posts?.get(adapterPosition), adapterPosition)
            listener?.onPostClicked(post)
        }
    }

    fun bind(post: Post?) {
        this.post = post
        titleTextView?.text = post?.title
        descriptionTextView?.text = post?.description
    }

}
