package com.example.mobile.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: String,
    val imageUri: String,
) {
    companion object {

        const val UID_NAME = "uid"
        const val IMAGE_URI_NAME = "imageUri"
        fun fromJson(json: Map<String, Any>): User {
            val uid = json[UID_NAME] as? String ?: ""
            val imageUri = json[IMAGE_URI_NAME] as? String ?: ""
            return User(uid, imageUri)
        }
    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                UID_NAME to uid,
                IMAGE_URI_NAME to imageUri
            )
        }
}