package com.example.booktrack.ui.my_profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.booktrack.LoginActivity
import com.example.booktrack.data.database.AppDatabase
import com.example.booktrack.data.repositories.UserRepository
import com.example.booktrack.data.viewModels.UserViewModel
import com.example.booktrack.databinding.FragmentMyProfileBinding
import kotlinx.coroutines.launch

class MyProfileFragment : Fragment() {

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: android.content.SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
        val userRepository = UserRepository(userDao)
        val userViewModel = UserViewModel(requireActivity().application, userRepository)


        sharedPref = requireContext().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)

        updateUserData()

        binding.btnEditUsername.setOnClickListener {
            showEditDialog("Edit Username", "username", binding.textUsername, "Username")
        }

        binding.btnEditEmail.setOnClickListener {
            showEditDialog("Edit Email", "email", binding.textEmail, "Mail")
        }

        binding.btnChangePassword.setOnClickListener {
            // Aici poți face navigare către un fragment de schimbare a parolei sau un dialog
        }


        // Logout
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(com.example.booktrack.R.menu.profile_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    com.example.booktrack.R.id.action_logout -> {
                        sharedPref.edit {
                            clear()
                            apply()
                        }

                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun updateUserData() {
        val username = sharedPref.getString("username", "N/A") ?: "N/A"
        val email = sharedPref.getString("email", "N/A") ?: "N/A"

        binding.textUsername.text = "Username - $username"
        binding.textEmail.text = "Mail - $email"
    }

    private fun showEditDialog(
        title: String,
        key: String,
        targetTextView: TextView,
        label: String
    ) {
        val input = EditText(requireContext())
        input.setText(sharedPref.getString(key, ""))

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val newValue = input.text.toString().trim()

                if (newValue.isNotBlank()) {
                    sharedPref.edit {
                        putString(key, newValue)
                        apply()
                    }

                    // actualizare în baza de date
                    lifecycleScope.launch {
                        val currentEmail = sharedPref.getString("email", "") ?: return@launch
                        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
                        val user = userDao.getUserByEmail(currentEmail)

                        if (user != null) {
                            val updatedUser = when (key) {
                                "username" -> user.copy(username = newValue)
                                "email" -> {
                                    sharedPref.edit {
                                        putString("email", newValue) // update și în SharedPrefs
                                    }
                                    user.copy(email = newValue)
                                }
                                else -> user
                            }

                            val userRepository = UserRepository(userDao)
                            val userViewModel = UserViewModel(requireActivity().application, userRepository)
                            userViewModel.updateUser(updatedUser)
                        }
                    }

                    targetTextView.text = "$label - $newValue"
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
