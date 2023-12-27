package com.example.mobile.Modules.Posts.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.Model.Post
import com.example.mobile.Modules.Posts.PostsRecyclerViewActivity
import com.example.mobile.R

class PostsRecyclerAdapter(var posts: MutableList<Post>?) :
    RecyclerView.Adapter<PostsViewHolder>() {

    var listener: PostsRecyclerViewActivity.OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_layout_row, parent, false)
        return PostsViewHolder(itemView, listener, posts)
    }

    override fun getItemCount(): Int = posts?.size ?: 0

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val post = posts?.get(position)
        holder.bind(post)
    }


}