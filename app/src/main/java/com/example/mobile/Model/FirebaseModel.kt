package com.example.mobile.Model

import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.ktx.Firebase

class FirebaseModel {
    private val db = Firebase.firestore

    companion object {
        const val POSTS_COLLECTION_PATH = "posts"
    }

    init {
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings { })
        }
        db.firestoreSettings = settings
    }

    fun getAllPosts(callback: (List<Post>) -> Unit) {
        db.collection(POSTS_COLLECTION_PATH)
            .get()
            .addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val posts: MutableList<Post> = mutableListOf()
                        for (json in it.result) {

                            val post = Post.fromJson(json.data)
                            posts.add(post)
                        }
                        callback(posts)
                    }

                    false -> {
                        callback(listOf())
                    }
                }
            }
    }

    fun addPost(post: Post, callback: () -> Unit) {
        val db = Firebase.firestore


        db.collection(POSTS_COLLECTION_PATH)
            .add(post.json)
            .addOnSuccessListener {
                callback()
            }
    }

}

