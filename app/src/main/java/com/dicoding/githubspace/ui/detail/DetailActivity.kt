package com.dicoding.githubspace.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.githubspace.R
import com.dicoding.githubspace.adapter.DetailUserPagerAdapter
import com.dicoding.githubspace.data.response.UserDetail
import com.dicoding.githubspace.data.response.Users
import com.dicoding.githubspace.databinding.ActivityDetailBinding
import com.dicoding.githubspace.ui.favorite.FavoriteActivity
import com.dicoding.githubspace.viewmodel.detail.DetailViewModel
import com.dicoding.githubspace.viewmodel.favorite.FavoriteViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var fav: Boolean = false
    private var profileHtmlUrl: String? = null
    private var profilename: String? = null

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val detailPagerAdapter = DetailUserPagerAdapter(this)
        binding.viewPagerDetail.adapter = detailPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPagerDetail) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()


        viewModel = obtainViewModel(this)

        val user = intent.getParcelableExtra<Users>(DATA_USER) as Users
        viewModel.dataUser = user.login

        viewModel.userdetails.observe(this) {
            onSuccessDetail(it)
        }

        viewModel.isLoading.observe(this) {
            onLoading(it)
        }

        viewModel.allList().observe(this) {
            fav = it.contains(user)
            if (fav) {
                binding.fabFav.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_filled))
            } else {
                binding.fabFav.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_border))
            }
        }

        binding.fabFav.setOnClickListener {
            if (fav) {
                viewModel.delete(user)
            } else {
                viewModel.insert(user)
                startActivity(Intent(this, FavoriteActivity::class.java))
            }
        }

    }

    private fun obtainViewModel(detailActivity: DetailActivity): DetailViewModel {
        val factory = FavoriteViewModelFactory.getInstance(detailActivity.application)
        return ViewModelProvider(detailActivity, factory)[DetailViewModel::class.java]
    }

    private fun onLoading(isLoading: Boolean) {
        binding.ProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) resetDetailUser()
    }

    private fun onSuccessDetail(detailuser: UserDetail) {
        with(binding) {
            tvrealname.text = detailuser.name ?: " - "
            tvusername.text = detailuser.login ?: " - "
            tvblog1.text = detailuser.blog ?: " - "
            tvcompany1.text = detailuser.company ?: " - "
            tvlocation1.text = detailuser.location ?: "-"
            tvbio.text = (detailuser.bio ?: "-") as CharSequence?
            tvFollowers.text = StringBuilder().append(detailuser.followers).append(" ")
            tvFollowing.text = StringBuilder().append(detailuser.following).append(" ")
            Glide.with(this@DetailActivity)
                .load(detailuser.avatarUrl)
                .into(ivprofilepicture)

        }
        val htmlUrl = detailuser.htmlUrl
        this.profileHtmlUrl = htmlUrl
        val nameprofile = detailuser.name
        this.profilename = nameprofile
    }

    private fun resetDetailUser() {
        with(binding) {
            tvrealname.text = ""
            tvusername.text = ""
            tvblog1.text = ""
            tvcompany1.text = ""
            tvlocation1.text = ""
            tvbio.text = ""
            tvFollowers.text = ""
            tvFollowing.text = ""

            Glide.with(this@DetailActivity)
                .load(ivprofilepicture)
                .into(ivprofilepicture)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.action_share -> {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                val shareText = "Check out the GitHub profile of $profilename: $profileHtmlUrl"
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
                startActivity(Intent.createChooser(shareIntent, "Bagikan link melalui"))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val DATA_USER = "data_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.title1,
            R.string.title2
        )
    }
}
