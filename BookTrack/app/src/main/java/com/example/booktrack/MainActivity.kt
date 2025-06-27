package com.example.booktrack

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.booktrack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inițializează binding PRIMA DATĂ!
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Acum poți folosi binding.navView
        val role = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("role", "user")
        if (role == "admin") {
            val navView: BottomNavigationView = binding.navView
            navView.menu.findItem(R.id.navigation_my_books)?.title = "Books"
        }

        val username = intent.getStringExtra("username")
        username?.let {
            Toast.makeText(this, "Welcome $it!", Toast.LENGTH_SHORT).show()
        }

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_my_books,
                R.id.navigation_search,
                R.id.navigation_my_profile,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}