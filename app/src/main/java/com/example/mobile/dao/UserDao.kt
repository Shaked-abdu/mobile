package com.example.mobile.dao

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobile.Model.User

interface UserDao {
    @Query("SELECT * FROM User")
    fun getAll(): LiveData<MutableList<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM User WHERE uid = :uid")
    fun getUserById(uid: String): LiveData<User>

}