package com.example.mobile.Model

import android.icu.text.CaseMap.Title
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.os.HandlerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.mobile.R
import com.example.mobile.dao.AppLocalDatabase
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executors

class Model private constructor() {
    enum class LoadingState {
        LOADED, LOADING
    }

    private val database = AppLocalDatabase.db
    private var executor = Executors.newSingleThreadExecutor()
    private var mainHelper = HandlerCompat.createAsync(Looper.getMainLooper())
    private val firebaseModel = FirebaseModel()
    private val posts: LiveData<MutableList<Post>>? = null
    val postsListLoadingState: MutableLiveData<LoadingState> = MutableLiveData(LoadingState.LOADED)

    companion object {
        val instance: Model = Model()
    }

    interface GetAllPostsListener {
        fun onComplete(posts: List<Post>)
    }

    fun getAllPosts(): LiveData<MutableList<Post>> {
        refreshAllPosts()
        return posts ?: database.postDao().getAll()
    }

    fun getMyPosts(user: String): LiveData<MutableList<Post>> {
        refreshAllPosts()
        return database.postDao().getMyPostsList(user)

    }

    fun refreshAllPosts() {
        postsListLoadingState.value = LoadingState.LOADING
        val lastUpdated: Long = Post.lastUpdated
        firebaseModel.getAllPosts(lastUpdated) { lists ->
            Log.i("TAG", "Firebase returned ${lists.size} posts. lastUpdated: $lastUpdated")
            executor.execute {
                var time = lastUpdated
                for (post in lists) {
                    database.postDao().insert(post)
                    post.lastUpdated?.let {
                        if (it > time)
                            time = post.lastUpdated ?: System.currentTimeMillis()
                    }
                }
                Post.lastUpdated = time
                postsListLoadingState.postValue(LoadingState.LOADED)
            }
        }
    }



    fun addPost(post: Post, callback: () -> Unit) {
        firebaseModel.addPost(post, callback)

    }

    fun addUser(user: User, callback: () -> Unit) {
        firebaseModel.addUser(user) {
            refreshAllPosts()
            callback()
        }
    }

    fun getUserById(uid: String, callback: (User?) -> Unit) {
        firebaseModel.getUserById(uid, callback)
    }

    fun updateUserById(user: User, callback: () -> Unit) {
        firebaseModel.updateUserById(user, callback)
    }

    fun deletePost(post: Post, callback: () -> Unit) {
        firebaseModel.deletePost(post, callback)
        executor.execute {
            database.postDao().deleteByUid(post.uid)
        }
    }

    fun updatePostByUid(post: Post, callback: () -> Unit) {
        firebaseModel.updatePostByUid(post, callback)
        executor.execute {
            database.postDao().updatePost(
                post.uid,
                post.title,
                post.description,
                post.imageUri,
                post.owner,
                System.currentTimeMillis()
            )
        }
    }

    fun getPostById(uid: String, callback: (Post?) -> Unit) {
        firebaseModel.getPostById(uid, callback)
    }
}