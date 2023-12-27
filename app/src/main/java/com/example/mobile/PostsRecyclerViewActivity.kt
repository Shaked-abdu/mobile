package com.example.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.Model.Model
import com.example.mobile.Model.Post

class PostsRecyclerViewActivity : AppCompatActivity() {
    var postsRecyclerView: RecyclerView? = null
    var posts: MutableList<Post>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        posts = Model.instance.posts
        setContentView(R.layout.activity_posts_recycler_view)
        postsRecyclerView = findViewById(R.id.rvPostsRecyclerList)
        postsRecyclerView?.setHasFixedSize(true)

        postsRecyclerView?.layoutManager = LinearLayoutManager(this)
        postsRecyclerView?.adapter = PostsRecyclerAdapter()

        val adapter = PostsRecyclerAdapter()
        adapter.listener = object : OnItemClickListener {
            override fun onItemClick(post: Post?, position: Int) {
                Log.i("TAG", "Item clicked: $position")
            }

            override fun onPostClicked(post: Post?) {
                Log.i("TAG", "Post clicked: $post")
            }
        }

        postsRecyclerView?.adapter = adapter

    }

    interface OnItemClickListener {
        fun onItemClick(post: Post?, position: Int)
        fun onPostClicked(post: Post?)
    }

    inner class PostsViewHolder(val itemView: View, listener: OnItemClickListener?) :
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

    inner class PostsRecyclerAdapter : RecyclerView.Adapter<PostsViewHolder>() {

        var listener: OnItemClickListener? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.post_layout_row, parent, false)
            return PostsViewHolder(itemView, listener)
        }

        override fun getItemCount(): Int = posts?.size ?: 0

        override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
            val post = posts?.get(position)
            holder.bind(post)
        }


    }
}