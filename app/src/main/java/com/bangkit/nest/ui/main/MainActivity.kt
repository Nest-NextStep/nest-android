package com.bangkit.nest.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkit.nest.R
import com.bangkit.nest.databinding.ActivityMainBinding
import com.bangkit.nest.ui.auth.AuthActivity
//import com.bangkit.nest.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    private val viewModel by viewModels<MainViewModel> {
//        ViewModelFactory.getInstance(this)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        viewModel.getSession().observe(this) { user ->
//            if (!user.isLogin) {
//                val intent = Intent(this, AuthActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                startActivity(intent)
//                finish()
//            }
//        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setStatusBarTextColor(isDark = true)
    }

    private fun setupView() {
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

    }

    private fun setStatusBarTextColor(isDark: Boolean) {
        val window = window
        val decorView = window.decorView

        val windowInsetsController = WindowCompat.getInsetsController(window, decorView)
        windowInsetsController.isAppearanceLightStatusBars = isDark
    }

    fun setBottomNavigationVisibility(isVisible: Boolean) {
        binding.navView.isVisible = isVisible
    }

}