package com.example.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.example.mobile.Model.Model
import com.example.mobile.Model.Post

class MainActivity : AppCompatActivity() {
    var postsListView: ListView? = null
    var posts: MutableList<Post>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        posts = Model.instance.posts
        val addPostButton: Button = findViewById(R.id.btnAddPost)
        addPostButton.setOnClickListener(::onAddPostButtonClicked)
        postsListView = findViewById(R.id.lvPostList)
        postsListView?.adapter = postsListAdapter(posts)

        postsListView?.setOnItemClickListener { parent, view, position, id ->
            Log.i("MainActivity", "Item clicked: $position")
        }
    }

    fun onAddPostButtonClicked(view: View) {
        val intent = Intent(this, AddPostActivity::class.java)
        startActivity(intent)
    }

    class postsListAdapter(val posts: MutableList<Post>?) : BaseAdapter() {
        override fun getCount(): Int = posts?.size ?: 0

        override fun getItem(position: Int): Any {
            return Any()
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val post = posts?.get(position)
            var view: View? = null
            if (convertView == null) {
                view = LayoutInflater.from(parent?.context)
                    .inflate(R.layout.post_layout_row, parent, false)
            }
            view = view ?: convertView

            val titleTextView: TextView? = view?.findViewById(R.id.postTitle)
            val descriptionTextView: TextView? = view?.findViewById(R.id.postDescription)
            titleTextView?.text = post?.title
            descriptionTextView?.text = post?.description

            return view!!
        }
    }
}