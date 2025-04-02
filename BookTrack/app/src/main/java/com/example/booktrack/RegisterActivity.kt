package com.example.booktrack

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
// import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.booktrack.data.database.AppDatabase
// import androidx.appcompat.widget.LinearLayoutCompat
import com.example.booktrack.data.models.User
import com.example.booktrack.data.repositories.UserRepository
import com.example.booktrack.data.viewModels.UserViewModel
import com.example.booktrack.databinding.ActivityRegisterBinding
import com.example.booktrack.utils.HashUtils

class RegisterActivity :AppCompatActivity(){
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)

        // Inițializare ViewModel
        val userDao = AppDatabase.getDatabase(application).userDao()
        val repository = UserRepository(userDao)
        userViewModel = UserViewModel(application, repository)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            var valid = true

            if (username.isEmpty()) {
                binding.etUsername.error = "Username is required"
                valid = false
            }
            if (email.isEmpty()) {
                binding.etEmail.error = "Email is required"
                valid = false
            }
            if (password.isEmpty()) {
                binding.etPassword.error = "Password is required"
                valid = false
            }

            if (!valid) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hashedPassword = HashUtils.sha256(password)

            val user = User(
                id = 0,
                role = "user",
                username = username,
                email = email,
                password = hashedPassword
            )


            userViewModel.addUser(user)

            sharedPref.edit().apply {
                putString("username", username)
                putString("email", email)
                putBoolean("is_logged_in", true)
                apply()
            }


            Toast.makeText(this, "User registered!", Toast.LENGTH_SHORT).show()
            val goToLogin = TextView(this).apply {
                text = getString(R.string.login_redirect)
                setTextColor(Color.BLUE)
                setPadding(0, 32, 0, 0)
            }
            binding.registerLayout.addView(goToLogin)

            goToLogin.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }

        }
    }

}