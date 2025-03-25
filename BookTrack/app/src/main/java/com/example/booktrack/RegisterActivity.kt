package com.example.booktrack

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
// import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
// import androidx.appcompat.widget.LinearLayoutCompat
import com.example.booktrack.data.database.UserDatabase
import com.example.booktrack.data.models.User
import com.example.booktrack.data.repositories.UserRepository
import com.example.booktrack.data.viewModels.UserViewModel
import com.example.booktrack.databinding.ActivityRegisterBinding

class RegisterActivity :AppCompatActivity(){
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inițializare ViewModel
        val userDao = UserDatabase.getDatabase(application).userDao()
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


            val user = User(
                id = 0,
                role = "user",
                username = username,
                email = email,
                password = password
            )

            userViewModel.addUser(user)

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