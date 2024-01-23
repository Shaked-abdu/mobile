package com.example.mobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.mobile.Modules.Posts.PostsFragmentDirections
import com.example.mobile.databinding.ActivityMainBinding
import com.example.mobile.databinding.ActivityRegisterBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null
    private var userUid: String? = null

    private var bottomNavigationView: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userUid = FirebaseAuth.getInstance().currentUser?.uid
        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(R.id.mainNavHost) as? NavHostFragment

        bottomNavigationView = binding.mainActiviryBottomNavigationView

        navController = navHostFragment?.navController
        navController?.let { NavigationUI.setupActionBarWithNavController(this, it) }
        navController?.let { NavigationUI.setupWithNavController(bottomNavigationView!!, it) }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navController?.navigateUp()
                true
            }

            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.remove("userUid")
                editor.apply()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
                true
            }

            else -> navController?.let { NavigationUI.onNavDestinationSelected(item, it) }
                ?: super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }


}