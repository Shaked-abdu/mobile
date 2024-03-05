package com.example.mobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.mobile.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton = binding.btnLogin
        val tvRegister = binding.tvRegister
        tvRegister.setOnClickListener {

            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        loginButton.setOnClickListener(::login)

        var userUid = checkIfUserIsSignedIn()
        if (userUid != "") {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.putExtra(
                "userUid",
                userUid
            )
            startActivity(intent)
            finish()
        }


    }

    private fun login(view: View) {
        val email = binding.etLoginEmail.text.toString()
        val password = binding.etLoginPassword.text.toString()
        when {
            TextUtils.isEmpty(email.trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@LoginActivity,
                    "Please enter email.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            TextUtils.isEmpty(password.trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@LoginActivity,
                    "Please enter password.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            saveUserIdInSharedPreferences(FirebaseAuth.getInstance().currentUser!!.uid)
                            Toast.makeText(
                                this@LoginActivity,
                                "You are logged in successfully.",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent =
                                Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            intent.putExtra(
//                                "user_id",
//                                FirebaseAuth.getInstance().currentUser!!.uid
//                            )
//                            intent.putExtra("email_id", email)
                            startActivity(intent)
                            finish()
                        } else {

                            Toast.makeText(
                                this@LoginActivity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

    }

    fun saveUserIdInSharedPreferences(uid: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userUid", uid)
        editor.apply()
    }

    fun checkIfUserIsSignedIn(): String {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userUid = sharedPreferences.getString("userUid", null)
        return userUid ?: ""
    }
}
