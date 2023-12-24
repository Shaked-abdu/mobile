package com.example.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class AddPostActivity : AppCompatActivity() {
    var descriptionTextField: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
    }

    private fun setupUI(){
        descriptionTextField = findViewById(R.id.etDescription)
    }
}