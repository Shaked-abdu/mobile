package com.example.mobile.Modules.Posts

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
import com.example.mobile.Modules.Posts.Adapter.PostsRecyclerAdapter
import com.example.mobile.R

class PostsRecyclerViewActivity : AppCompatActivity() {
    var postsRecyclerView: RecyclerView? = null
    var posts: List<Post>? = null
    var adapter: PostsRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts_recycler_view)
        adapter = PostsRecyclerAdapter(posts)

        Model.instance.getAllPosts { posts ->
            this.posts = posts
            adapter?.posts = posts
            adapter?.notifyDataSetChanged()
        }
        postsRecyclerView = findViewById(R.id.rvPostsRecyclerList)
        postsRecyclerView?.setHasFixedSize(true)

        postsRecyclerView?.layoutManager = LinearLayoutManager(this)

        adapter?.listener = object : OnItemClickListener {
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

    override fun onResume() {
        super.onResume()
        Model.instance.getAllPosts { posts ->
            this.posts = posts
            adapter?.posts = posts
            adapter?.notifyDataSetChanged()
        }
    }
}