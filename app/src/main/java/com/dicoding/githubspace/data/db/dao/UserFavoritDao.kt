package com.dicoding.githubspace.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.githubspace.data.response.Users

@Dao
interface UserFavoritDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(users: Users)

    @Delete
    fun delete(users: Users)

    @Query("SELECT * from users ORDER BY login ASC")
    fun getFavUser(): LiveData<List<Users>>

}