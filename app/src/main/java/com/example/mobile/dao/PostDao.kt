package com.example.mobile.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobile.Model.Post

@Dao
interface PostDao {
    @Query("SELECT * FROM Post")
    fun getAll(): LiveData<MutableList<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg posts: Post)

    @Delete
    fun delete(post: Post)

    @Query("DELETE FROM Post")
    fun deleteAll()

    @Query("DELETE FROM Post WHERE uid = :uid")
    fun deleteByUid(uid: String)

    @Query("SELECT * FROM Post WHERE owner = :owner")
    fun getByOwner(owner: String): LiveData<MutableList<Post>>
}