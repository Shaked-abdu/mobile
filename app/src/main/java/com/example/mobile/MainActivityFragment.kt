package com.example.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView

class MainActivityFragment : AppCompatActivity() {
    var blueFragment: BlueFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fragment)

        val button: Button = findViewById(R.id.btn)
        button.setOnClickListener(::onButtonClicked)

    }

    fun displayBlueFragment() {
        blueFragment = BlueFragment.newInstance("Blue Fragment")
        blueFragment?.let {fragment ->
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.flMainFragment, fragment)
            transaction.addToBackStack("TAG")
            transaction.commit()
        }
    }

    fun onButtonClicked(view: View) {
        if(blueFragment == null){
            displayBlueFragment()
        }else{
            removeBlueFragment()
        }
    }

    fun removeBlueFragment() {
        blueFragment?.let {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.remove(it)
            transaction.addToBackStack("TAG")
            transaction.commit()
        }
        blueFragment = null
    }
}