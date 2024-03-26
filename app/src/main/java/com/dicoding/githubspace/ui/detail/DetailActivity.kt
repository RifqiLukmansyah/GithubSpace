package com.dicoding.githubspace.ui.detail

import android.content.Intent
import android.os.Bundle
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
import com.dicoding.githubspace.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailPagerAdapter = DetailUserPagerAdapter(this)
        binding.viewPagerDetail.adapter = detailPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPagerDetail) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[DetailViewModel::class.java]

        if (viewModel.dataUser.isEmpty()) {
            val user = intent.getParcelableExtra<Users>(DATA_USER) as Users
            viewModel.dataUser = user.login.toString()
        }

        viewModel.userdetails.observe(this) {
            onSuccessDetail(it)
        }

        viewModel.isLoading.observe(this) {
            onLoading(it)
        }

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

            ivback.setOnClickListener(View.OnClickListener { finish() })

            ivshare.setOnClickListener(View.OnClickListener {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, detailuser.htmlUrl)
                startActivity(Intent.createChooser(shareIntent, "Bagikan link melalui"))
            })
        }
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

    companion object {
        const val DATA_USER = "data_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.title1,
            R.string.title2
        )
    }
}
