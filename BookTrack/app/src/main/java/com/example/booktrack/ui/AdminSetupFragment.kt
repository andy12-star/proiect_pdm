package com.example.booktrack.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.booktrack.data.database.AppDatabase
import com.example.booktrack.data.models.User
import com.example.booktrack.utils.HashUtils
import kotlinx.coroutines.launch

class AdminSetupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Nu ai nevoie de layout vizual, returnează un View gol
        return View(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDao = AppDatabase.getDatabase(requireContext()).userDao()

        lifecycleScope.launch {
            val existingAdmin = userDao.getUserByEmail("admin@example.com")
            if (existingAdmin == null) {
                val adminUser = User(
                    id = 0,
                    role = "admin",
                    username = "admin",
                    email = "admin@example.com",
                    password = HashUtils.sha256("admin123")
                )

                userDao.addUser(adminUser)
                Toast.makeText(requireContext(), "Admin creat cu succes!", Toast.LENGTH_SHORT).show()

                with(requireContext().getSharedPreferences("admin_setup", AppCompatActivity.MODE_PRIVATE).edit()) {
                    putBoolean("is_admin_created", true)
                    apply()
                }

                requireActivity().recreate()
            } else {
                Toast.makeText(requireContext(), "Adminul există deja.", Toast.LENGTH_SHORT).show()
                requireActivity().recreate()
            }
        }

    }
}
