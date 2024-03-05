package com.example.mobile.Model

import com.google.firebase.Timestamp
import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mobile.base.MyApplication
import com.google.firebase.firestore.FieldValue

@Entity
data class Post(
    @PrimaryKey val uid: String,
    val title: String,
    val description: String,
    val owner: String,
    val imageUri: String,
    var lastUpdated: Long? = null
) {
    companion object {

        var lastUpdated: Long
            get(){
                return MyApplication.Globals.appContext
                    ?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    ?.getLong(GET_LAST_UPDATED, 0) ?: 0
            }
            set(value) {
                MyApplication.Globals.appContext
                    ?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    ?.edit()
                    ?.putLong(GET_LAST_UPDATED, value)
                    ?.apply()
            }
        const val UID_NAME = "uid"
        const val TITLE_NAME = "title"
        const val DESCRIPTION_NAME = "description"
        const val OWNER_NAME = "owner"
        const val IMAGE_URI_NAME = "imageUri"
        const val LAST_UPDATED_NAME = "lastUpdated"
        const val GET_LAST_UPDATED = "get_last_updated"
        fun fromJson(json: Map<String, Any>): Post {
            val uid = json[User.UID_NAME] as? String ?: ""
            val title = json[TITLE_NAME] as? String ?: ""
            val description = json[DESCRIPTION_NAME] as? String ?: ""
            val owner = json[OWNER_NAME] as? String ?: ""
            val imageUri = json[IMAGE_URI_NAME] as? String ?: ""
            val post =  Post(uid, title, description, owner, imageUri)

            val timestamp: Timestamp? = json[LAST_UPDATED_NAME] as? Timestamp
            timestamp?.let {
                post.lastUpdated = it.seconds
            }

            return post


        }
    }

    val json: Map<String, Any>
        get() {
            return hashMapOf(
                UID_NAME to uid,
                TITLE_NAME to title,
                DESCRIPTION_NAME to description,
                OWNER_NAME to owner,
                IMAGE_URI_NAME to imageUri,
                LAST_UPDATED_NAME to FieldValue.serverTimestamp()
            )
        }
}