package com.dicoding.githubspace.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubspace.R
import com.dicoding.githubspace.adapter.UserListAdapter
import com.dicoding.githubspace.data.response.Users
import com.dicoding.githubspace.databinding.ActivityHomeBinding
import com.dicoding.githubspace.ui.detail.DetailActivity
import com.dicoding.githubspace.ui.favorite.FavoriteActivity
import com.dicoding.githubspace.ui.setting.SettingActivity
import com.dicoding.githubspace.viewmodel.home.HomeViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var userListAdapter: UserListAdapter

    private lateinit var rvuser: RecyclerView
    private var dataUser = listOf<Users>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rvuser = findViewById(R.id.rvuser)
        with(binding) {
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.queryHint = getString(R.string.hintForSearch)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        homeViewModel.findSearchUser(it)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })

        }

        userListAdapter = UserListAdapter(dataUser)
        val layoutManager = LinearLayoutManager(this)
        binding.rvuser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvuser.addItemDecoration(itemDecoration)

        homeViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        )[HomeViewModel::class.java]

        homeViewModel.user.observe(this) { user ->
            if (user != null) {
                dataUser = user
                setDataListUser(dataUser)
            }
        }

        homeViewModel.mainLoading.observe(this) {
            onLoading(it)
        }

        homeViewModel.userList.observe(this@HomeActivity) { userList ->
            setDataListUser(userList)
        }

    }

    private fun setDataListUser(githubList: List<Users>) {
        binding.textnotfound.visibility = if (githubList.isEmpty()) View.VISIBLE else View.GONE
        userListAdapter = UserListAdapter(githubList)
        binding.rvuser.apply {
            adapter = userListAdapter
            layoutManager =
                LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)

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

    private fun onLoading(loading: Boolean) {
        binding.homeProgressBar.visibility = if (loading) View.VISIBLE else View.GONE
        if (loading) resetListUser()
    }

    private fun resetListUser() {
        with(binding) {
            rvuser.adapter = null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_list -> {
                rvuser.layoutManager = LinearLayoutManager(this)
                rvuser.adapter = userListAdapter
            }

            R.id.action_grid -> {
                rvuser.layoutManager = GridLayoutManager(this, 2)
                rvuser.layoutManager = GridLayoutManager(this, 2)
                rvuser.adapter = userListAdapter
            }

            R.id.action_favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }

            R.id.action_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}