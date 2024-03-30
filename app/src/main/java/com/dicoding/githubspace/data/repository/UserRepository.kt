package com.dicoding.githubspace.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubspace.data.db.dao.UserFavoritDao
import com.dicoding.githubspace.data.db.room.UserFavoritDatabase
import com.dicoding.githubspace.data.response.Users
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val favDao: UserFavoritDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserFavoritDatabase.getDatabase(application)
        favDao = db.UserFavoritDao()
    }

    fun getFavoriteUsers(): LiveData<List<Users>> = favDao.getFavUser()

    fun insert(user: Users) {
        executorService.execute { favDao.insert(user) }
    }

    fun delete(user: Users) {
        executorService.execute { favDao.delete(user) }
    }
}