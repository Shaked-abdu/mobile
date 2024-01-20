package com.example.mobile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.example.mobile.Model.Model
import com.example.mobile.Model.User
import com.example.mobile.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var storageRef: StorageReference
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storageRef = FirebaseStorage.getInstance().reference.child("profile_images")
        val registerButton = binding.btnRegister
        val tvLogin = binding.tvLogin
        val ivUploadImage = binding.ivRegisterUploadImage

        registerButton.setOnClickListener(::register)
        ivUploadImage.setOnClickListener(::uploadImage)

        tvLogin.setOnClickListener {

            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

    }

    private fun register(view: View) {
        binding.progressBar.visibility = View.VISIBLE
        val email = binding.etRegisterEmail.text.toString()
        val password = binding.etRegisterPassword.text.toString()
        when {
            TextUtils.isEmpty(email.trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please enter email.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            TextUtils.isEmpty(password.trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please enter password.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            storageRef = storageRef.child(System.currentTimeMillis().toString())
                            imageUri?.let {
                                storageRef.putFile(it).addOnCompleteListener() { task ->
                                    if (task.isSuccessful) {

                                        storageRef.downloadUrl.addOnSuccessListener { uri ->

                                            saveUser(email, uri.toString())
                                        }
                                    } else {
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            task.exception?.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                        } else {

                            binding.progressBar.visibility = View.GONE
                            Log.i("RegisterActivity", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                this@RegisterActivity,
                                "${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }
        }
    }

    private fun uploadImage(view: View) {
        resultLauncher.launch("image/*")
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
        binding.ivRegisterUploadImage.setImageURI(it)
    }

    private fun saveUser(email: String, imageUri: String) {
        val user = User(email, imageUri)
        Model.instance.addUser(user) {
            Toast.makeText(
                this@RegisterActivity,
                "Registration Successful.",
                Toast.LENGTH_SHORT
            ).show()

            binding.progressBar.visibility = View.GONE
            binding.ivRegisterUploadImage.setImageResource(R.drawable.vector)
            startActivity(
                Intent(
                    this@RegisterActivity,
                    LoginActivity::class.java
                )
            )
        }
    }

}