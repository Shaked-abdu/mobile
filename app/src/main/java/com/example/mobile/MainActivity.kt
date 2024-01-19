package com.example.mobile

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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    private var email: String? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private val bundle = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        email = FirebaseAuth.getInstance().currentUser?.email
        bundle.putString("EMAIL", email)

        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(R.id.mainNavHost) as? NavHostFragment

        bottomNavigationView =
            findViewById(R.id.mainActiviryBottomNavigationView)

        navController = navHostFragment?.navController
        navController?.let { NavigationUI.setupActionBarWithNavController(this, it) }
        navController?.let { NavigationUI.setupWithNavController(bottomNavigationView!!, it) }


//        val navController = findNavController(R.id.nav_host_fragment)
//        navController?.navigate(R.id.addPostFragment, bundle)
//        navController?.navigate(R.id.postsFragment, bundle)


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navController?.navigateUp()
                true
            }

            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
                true
            }

            R.id.addPostFragment -> {
                Log.i("EMAIL", "Email: $email")
                Log.i("GAT", "clicked")
                val action =
                    PostsFragmentDirections.actionPostsFragmentToAddPostFragment(email ?: "")
                navController?.navigate(action)
                return true
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