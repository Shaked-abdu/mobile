package com.example.mobile.Model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.ktx.Firebase

class FirebaseModel {
    private val db = Firebase.firestore

    companion object {
        const val POSTS_COLLECTION_PATH = "posts"
        const val USERS_COLLECTION_PATH = "users"
    }

    init {
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings { })
        }
        db.firestoreSettings = settings
    }

    fun getAllPosts(since: Long, callback: (List<Post>) -> Unit) {
        db.collection(POSTS_COLLECTION_PATH)
            .whereGreaterThanOrEqualTo(Post.LAST_UPDATED_NAME, Timestamp(since, 0))
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

    fun getAllMyPosts(since: Long, callback: (List<Post>) -> Unit) {
        db.collection(POSTS_COLLECTION_PATH)
            .whereGreaterThanOrEqualTo(Post.LAST_UPDATED_NAME, Timestamp(since, 0))
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

    fun addUser(user: User, callback: () -> Unit) {
        val db = Firebase.firestore
        db.collection(USERS_COLLECTION_PATH)
            .document(user.uid)
            .set(user.json)
            .addOnSuccessListener() {
                callback()
            }
    }

    fun getUserById(uid: String, callback: (User?) -> Unit) {
        db.collection(USERS_COLLECTION_PATH)
            .whereEqualTo(User.UID_NAME, uid)
            .get()
            .addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val users: MutableList<User> = mutableListOf()
                        for (json in it.result) {
                            val user = User.fromJson(json.data)
                            users.add(user)
                        }
                        callback(users.firstOrNull())
                    }

                    false -> {
                        callback(null)
                    }
                }
            }
    }

    fun updateUserById(user: User, callback: () -> Unit) {
        addUser(user, callback)
    }
}


