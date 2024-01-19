package com.example.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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

                            Toast.makeText(
                                this@LoginActivity,
                                "You are logged in successfully.",
                                Toast.LENGTH_SHORT
                            ).show()

                            /**
                             * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
                             * and send him to Main Screen with user id and email that user have used for registration.
                             */

                            val intent =
                                Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra(
                                "user_id",
                                FirebaseAuth.getInstance().currentUser!!.uid
                            )
                            intent.putExtra("email_id", email)
                            startActivity(intent)
                            finish()
                        } else {

                            // If the login is not successful then show error message.
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
    // END
}