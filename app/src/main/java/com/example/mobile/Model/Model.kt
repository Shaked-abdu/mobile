package com.example.mobile.Model

import android.os.Looper
import androidx.core.os.HandlerCompat
import com.example.mobile.dao.AppLocalDatabase
import java.util.concurrent.Executors

class Model private constructor() {

    private val database = AppLocalDatabase.db
    private var executor = Executors.newSingleThreadExecutor()
    private var mainHelper = HandlerCompat.createAsync(Looper.getMainLooper())

    companion object {
        val instance: Model = Model()
    }

    interface GetAllPostsListener {
        fun onComplete(posts: List<Post>)
    }

    fun getAllPosts(callback: (List<Post>) -> Unit) {
        executor.execute {
            Thread.sleep(5000)
            val posts = database.postDao().getAll()
            mainHelper.post {
                callback(posts)
            }
        }
    }

    fun addPost(post: Post, callback: () -> Unit) {
        executor.execute {
            database.postDao().insert(post)
            mainHelper.post {
                callback()
            }
        }
    }
}

