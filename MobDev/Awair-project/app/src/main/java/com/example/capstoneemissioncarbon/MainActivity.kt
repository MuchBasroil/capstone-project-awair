package com.example.capstoneemissioncarbon

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.capstoneemissioncarbon.databinding.ActivityMainBinding
import com.example.capstoneemissioncarbon.ui.profile.ProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout with ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Hide ActionBar
        supportActionBar?.hide()

        // Initialize BottomNavigationView
        val navView: BottomNavigationView = binding.navView

        // Set up Navigation Controller
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Set up AppBarConfiguration for BottomNavigation to avoid changing ActionBar title
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.history,
                R.id.profile
            )
        )

        // Connect BottomNavigationView with NavController
        navView.setupWithNavController(navController)

    }
}
