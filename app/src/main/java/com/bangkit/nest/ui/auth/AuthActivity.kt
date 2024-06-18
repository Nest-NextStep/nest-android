package com.bangkit.nest.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.bangkit.nest.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarTextColor(true)
    }

    private fun setStatusBarTextColor(isDark: Boolean) {
        val window = window
        val decorView = window.decorView

        val windowInsetsController = WindowCompat.getInsetsController(window, decorView)
        windowInsetsController.isAppearanceLightStatusBars = isDark
    }

}