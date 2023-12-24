package com.example.mobile.Model

class Model private constructor() {

    val posts: MutableList<Post> = ArrayList()

    companion object {
        val instance: Model = Model()
    }

    init{
        for(i in 1..10){
            posts.add(Post(" דיוח$i", " תיאור דיוח$i"))      }
    }
}