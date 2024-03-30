package com.dicoding.githubspace.viewmodel.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubspace.data.repository.UserRepository
import com.dicoding.githubspace.data.response.Users

class FavoriteViewModel(application: Application) : ViewModel() {
    private val favoriteUserRepo: UserRepository = UserRepository(application)

    fun getFavoriteUsers(): LiveData<List<Users>> = favoriteUserRepo.getFavoriteUsers()
}