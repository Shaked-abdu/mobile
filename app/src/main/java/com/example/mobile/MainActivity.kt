package com.example.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    var lvPosts: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addPostButton: Button = findViewById(R.id.btnAddPost)
        addPostButton.setOnClickListener(::onAddPostButtonClicked)
        lvPosts = findViewById(R.id.lvPosts)
    }

    fun onAddPostButtonClicked(view: View){
        val intent = Intent(this, AddPostActivity::class.java)
        startActivity(intent)
    }
}