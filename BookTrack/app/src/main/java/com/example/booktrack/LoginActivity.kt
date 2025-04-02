package com.example.booktrack

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.booktrack.data.database.AppDatabase
import com.example.booktrack.data.repositories.UserRepository
import com.example.booktrack.data.viewModels.UserViewModel
import com.example.booktrack.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import com.example.booktrack.utils.HashUtils


class LoginActivity:AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // verificare user daca e logat
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // daca nu e logat, atunci login
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDao = AppDatabase.getDatabase(application).userDao()
        val repository = UserRepository(userDao)
        userViewModel = UserViewModel(application, repository)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            var valid = true

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

            lifecycleScope.launch {
                val hashedPassword = HashUtils.sha256(password)
                val user = userViewModel.login(email, hashedPassword)

                if (user != null) {
                    Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("username", user.username)

                    with(sharedPref.edit()) {
                        putBoolean("is_logged_in", true)
                        putString("username", user.username)
                        putString("email", user.email)
                        apply()
                    }

                    startActivity(intent)

                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }


        // Navigare către RegisterActivity
        binding.tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}