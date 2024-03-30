package com.dicoding.githubspace.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubspace.adapter.UserListAdapter
import com.dicoding.githubspace.data.response.Users
import com.dicoding.githubspace.databinding.ActivityFavoriteBinding
import com.dicoding.githubspace.ui.detail.DetailActivity
import com.dicoding.githubspace.viewmodel.favorite.FavoriteViewModel
import com.dicoding.githubspace.viewmodel.favorite.FavoriteViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var _activityFavBinding: ActivityFavoriteBinding
    private val binding get() = _activityFavBinding
    private var dataUser = listOf<Users>()
    private lateinit var userListAdapter: UserListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFavBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Favorite"
        val favViewModel = obtainViewModel(this)
        favViewModel.getFavoriteUsers().observe(this) { listUser ->
            if (listUser.isNotEmpty()) {
                setDataListUser(listUser)
                binding.txtNofav.visibility = View.GONE
            } else {
                binding.rvuser.visibility = View.GONE
                binding.txtNofav.visibility = View.VISIBLE
            }
        }

        userListAdapter = UserListAdapter(dataUser)

        binding.rvuser.layoutManager = LinearLayoutManager(this)
        binding.rvuser.setHasFixedSize(true)
        binding.rvuser.adapter = userListAdapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun setDataListUser(githubList: List<Users>) {
        userListAdapter = UserListAdapter(githubList)
        binding.rvuser.apply {
            adapter = userListAdapter
            layoutManager =
                LinearLayoutManager(this@FavoriteActivity, LinearLayoutManager.VERTICAL, false)

            userListAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Users) {
                    clickToDetail(data)
                }
            })
        }
    }

    private fun clickToDetail(data: Users) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.DATA_USER, data)
        startActivity(intent)
    }
}