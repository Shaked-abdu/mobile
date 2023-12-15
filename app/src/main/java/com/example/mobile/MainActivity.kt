package com.example.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.TextView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        textView.text = "hello"

        setContentView(textView)
    }
}
