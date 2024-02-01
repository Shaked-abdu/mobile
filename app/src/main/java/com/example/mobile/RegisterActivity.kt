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
import com.example.mobile.Model.StorageModel
import com.example.mobile.Model.User
import com.example.mobile.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val storageModel = StorageModel("profile_images")
    private var imageUri: Uri? = null
    private var imageUploaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val firstName = binding.etRegisterFirstName.text.toString()
        val lastName = binding.etRegisterLastName.text.toString()
        when {
            TextUtils.isEmpty(email.trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please enter email.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility = View.GONE
            }

            TextUtils.isEmpty(password.trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please enter password.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility = View.GONE
            }

            TextUtils.isEmpty(firstName.trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please enter first name.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility = View.GONE
            }

            TextUtils.isEmpty(lastName.trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please enter last name.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility = View.GONE
            }

            imageUploaded.not() -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please upload an image.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility = View.GONE
            }

            else -> {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            storageModel.uploadFile(imageUri!!,
                                onComplete = { uri ->
                                    val uid = FirebaseAuth.getInstance().currentUser!!.uid
                                    saveUser(uid, email, uri, firstName, lastName)
                                },
                                onFailure = { message ->

                                    Toast.makeText(
                                        this@RegisterActivity,
                                        message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
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
        if (it != null) {
            imageUri = it
            binding.ivRegisterUploadImage.setImageURI(it)
            imageUploaded = true
        }
    }

    private fun saveUser(
        uid: String,
        email: String,
        imageUri: String,
        firstName: String,
        lastName: String
    ) {
        val user = User(uid, email, imageUri, firstName, lastName)
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