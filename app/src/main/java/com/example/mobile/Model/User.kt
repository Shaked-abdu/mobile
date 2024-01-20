package com.example.mobile.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val email: String,
    val imageUri: String,
) {
    companion object {

        const val EMAIL_NAME = "email"
        const val IMAGE_URI_NAME = "imageUri"
        fun fromJson(json: Map<String, Any>): User {
            val email = json[EMAIL_NAME] as? String ?: ""
            val imageUri = json[IMAGE_URI_NAME] as? String ?: ""
            return User(email, imageUri)
        }
    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                EMAIL_NAME to email,
                IMAGE_URI_NAME to imageUri
            )
        }
}