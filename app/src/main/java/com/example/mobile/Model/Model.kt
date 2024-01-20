package com.example.mobile.Model

import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import com.example.mobile.dao.AppLocalDatabase
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executors

class Model private constructor() {

    private val database = AppLocalDatabase.db
    private var executor = Executors.newSingleThreadExecutor()
    private var mainHelper = HandlerCompat.createAsync(Looper.getMainLooper())
    private val firebaseModel = FirebaseModel()

    companion object {
        val instance: Model = Model()
    }

    interface GetAllPostsListener {
        fun onComplete(posts: List<Post>)
    }

    fun getAllPosts(callback: (List<Post>) -> Unit) {
        firebaseModel.getAllPosts(callback)
//        executor.execute {
//            Thread.sleep(5000)
//            val posts = database.postDao().getAll()
//            mainHelper.post {
//                callback(posts)
//            }
//        }
    }

    fun addPost(post: Post, callback: () -> Unit) {
        firebaseModel.addPost(post, callback)
//        executor.execute {
//            database.postDao().insert(post)
//            mainHelper.post {
//                callback()
//            }
//        }
    }
    fun addUser(user: User, callback: () -> Unit) {
        firebaseModel.addUser(user, callback)
    }
}