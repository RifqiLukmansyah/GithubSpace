package com.dicoding.githubspace.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubspace.data.response.UserDetail
import com.dicoding.githubspace.data.response.Users
import com.dicoding.githubspace.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    var dataUser: String = ""
        set(value) {
            field = value
            getDetailUser()
            getFollowers()
            getFollowings()
        }
    private val _userdetail = MutableLiveData<UserDetail>()
    val userdetails: LiveData<UserDetail> = _userdetail

    private val _loadingDetail = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _loadingDetail

    private val _followers = MutableLiveData<List<Users>>()
    val followers: LiveData<List<Users>> = _followers

    private val _followings = MutableLiveData<List<Users>>()
    val followings: LiveData<List<Users>> = _followings


    private fun getDetailUser() {
        _loadingDetail.value = true
        val api = ApiConfig.getApiService().getDetailUser(dataUser, HomeViewModel.APICode)
        api.enqueue(object : Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                _loadingDetail.value = false
                _userdetail.value = if (response.isSuccessful) response.body()!! else null
                if (!response.isSuccessful) {
                    Log.e(HomeViewModel.TAG, "error: ${response.message()}")
                }
            }


            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                _loadingDetail.value = false
                Log.e(HomeViewModel.TAG, "error: ${t.message}")
            }
        })
    }

    private fun getFollowers() {
        _loadingDetail.value = true
        val api = ApiConfig.getApiService().getUserFollowers(dataUser, HomeViewModel.APICode)
        api.enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                _loadingDetail.value = false
                _followers.value = if (response.isSuccessful) response.body()!! else null
                if (!response.isSuccessful) {
                    Log.e(HomeViewModel.TAG, "error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                _loadingDetail.value = false
                Log.e(HomeViewModel.TAG, "error: ${t.message}")
            }
        })
    }

    private fun getFollowings() {
        _loadingDetail.value = true
        val api = ApiConfig.getApiService().getUserFollowings(dataUser, HomeViewModel.APICode)
        api.enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                _loadingDetail.value = false
                _followings.value = if (response.isSuccessful) response.body()!! else null
                if (!response.isSuccessful) {
                    Log.e(HomeViewModel.TAG, "error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                _loadingDetail.value = false
                Log.e(HomeViewModel.TAG, "error: ${t.message}")
            }
        })
    }


}