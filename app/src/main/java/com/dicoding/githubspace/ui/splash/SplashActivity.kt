package com.dicoding.githubspace.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubspace.R
import com.dicoding.githubspace.data.response.SettingPreferences
import com.dicoding.githubspace.data.response.dataStore
import com.dicoding.githubspace.databinding.ActivitySplashBinding
import com.dicoding.githubspace.ui.home.HomeActivity
import com.dicoding.githubspace.viewmodel.setting.SettingThemeViewModel
import com.dicoding.githubspace.viewmodel.setting.SettingThemeViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, splashtimeOut.toLong())
        val pref = SettingPreferences.getInstance(application.dataStore)
        val themeViewModel = ViewModelProvider(
            this,
            SettingThemeViewModelFactory(pref)
        )[SettingThemeViewModel::class.java]
        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.splashscreen.setBackgroundColor(ContextCompat.getColor(this, androidx.appcompat.R.color.primary_material_dark))
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.splashscreen.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            }
        }
    }


    companion object {
        const val splashtimeOut = 1500
    }
}
