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
import com.example.booktrack.data.viewModels.UserViewModel

class MainActivity : AppCompatActivity() {

private lateinit var binding: ActivityMainBinding
private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra("username")
        username?.let {
            Toast.makeText(this, "Welcome $it!", Toast.LENGTH_SHORT).show()
        }


        binding = ActivityMainBinding.inflate(layoutInflater)
     setContentView(binding.root)

//----------------------partea de testare----------------------
//        // Initializarea UserViewModel
//        val userDao = UserDatabase.getDatabase(application).userDao()
//        val userRepository = UserRepository(userDao)
//        userViewModel = ViewModelProvider(this, UserViewModelFactory(application, userRepository)).get(UserViewModel::class.java)
//
//        // Adăugăm un utilizator de test în baza de date la lansarea aplicației
//        addTestUser()
//-------------------------------------------------------------

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_my_books, R.id.navigation_my_profile, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

//----------------------partea de testare----------------------
//    private fun addTestUser() {
//        // Creează un utilizator de test
//        val newUser = User(id = 0, role = "admin", username = "testAdmin", email = "test_admin@example.com", password = "hashed_password")
//
//        // Adaugă utilizatorul în baza de date folosind un thread de background
//        CoroutineScope(Dispatchers.IO).launch {
//            userViewModel.addUser(newUser)
//        }
//    }
//-------------------------------------------------------------

}