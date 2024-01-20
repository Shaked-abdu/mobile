package com.example.mobile.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(
    @PrimaryKey val title: String,
    val description: String,
    val owner: String,
    val imageUri: String
) {
    companion object {

        const val TITLE_NAME = "title"
        const val DESCRIPTION_NAME = "description"
        const val OWNER_NAME = "owner"
        const val IMAGE_URI_NAME = "imageUri"
        fun fromJson(json: Map<String, Any>): Post {
            val title = json[TITLE_NAME] as? String ?: ""
            val description = json[DESCRIPTION_NAME] as? String ?: ""
            val owner = json[OWNER_NAME] as? String ?: ""
            val imageUri = json[IMAGE_URI_NAME] as? String ?: ""
            return Post(title, description, owner, imageUri)
        }
    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                TITLE_NAME to title,
                DESCRIPTION_NAME to description,
                OWNER_NAME to owner,
                IMAGE_URI_NAME to imageUri
            )
        }
}