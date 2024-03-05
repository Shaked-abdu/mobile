package com.example.mobile.Modules.Posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mobile.Model.Post

class PostsViewModel : ViewModel() {
    var posts: LiveData<MutableList<Post>>? = null
    var myPosts : LiveData<MutableList<Post>>? = null


}