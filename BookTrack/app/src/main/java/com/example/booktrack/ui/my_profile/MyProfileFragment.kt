package com.example.booktrack.ui.my_profile

import NotificationViewModel
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.booktrack.LoginActivity
import com.example.booktrack.R
import com.example.booktrack.data.database.AppDatabase
import com.example.booktrack.data.models.Notification
import com.example.booktrack.data.repositories.NotificationRepository
import com.example.booktrack.data.repositories.UserRepository
import com.example.booktrack.data.viewModels.NotificationViewModelFactory
import com.example.booktrack.data.viewModels.UserViewModel
import com.example.booktrack.databinding.FragmentMyProfileBinding
import com.example.booktrack.utils.HashUtils
import kotlinx.coroutines.launch

class MyProfileFragment : Fragment() {

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: android.content.SharedPreferences
    private lateinit var notificationViewModel: NotificationViewModel

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

        val notificationDao = AppDatabase.getDatabase(requireContext()).notificationDao()
        val notificationRepo = NotificationRepository(notificationDao)
        val notificationFactory = NotificationViewModelFactory(requireActivity().application, notificationRepo)
        notificationViewModel = ViewModelProvider(requireActivity(), notificationFactory)[NotificationViewModel::class.java]


        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
        val userRepository = UserRepository(userDao)
        val userViewModel = UserViewModel(requireActivity().application, userRepository)

        sharedPref = requireContext().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)

        updateUserData()
        val role = sharedPref.getString("role", "user")
        if (role == "admin") {
            binding.btnEditUsername.visibility = View.GONE
            binding.btnEditEmail.visibility = View.GONE
            binding.btnChangePassword.visibility = View.GONE
            binding.btnDeleteAccount.visibility = View.GONE
        }

        binding.btnEditUsername.setOnClickListener {
            showEditDialog("Edit Username", "username", binding.textUsername, "Username", userViewModel)
        }

        binding.btnEditEmail.setOnClickListener {
            showEditDialog("Edit Email", "email", binding.textEmail, "Mail", userViewModel)
        }

        binding.btnChangePassword.setOnClickListener {
            showChangePasswordDialog(userViewModel)
        }

        binding.btnDeleteAccount.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("Yes") { _, _ ->
                    deleteUserAccount()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }


        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.profile_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_logout -> {
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

    private fun deleteUserAccount() {
        val email = sharedPref.getString("email", null)

        if (email.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Email not found", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val userDao = db.userDao()

            val user = userDao.getUserByEmail(email)
            if (user != null) {
                userDao.delete(user)
            }

            sharedPref.edit {
                clear()
                apply()
            }

            Toast.makeText(requireContext(), "Account deleted", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun showEditDialog(
        title: String,
        key: String,
        targetTextView: TextView,
        label: String,
        userViewModel: UserViewModel
    ) {
        val input = EditText(requireContext())
        input.setText(sharedPref.getString(key, ""))

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val newValue = input.text.toString().trim()

                if (newValue.isNotBlank()) {
                    lifecycleScope.launch {
                        val oldEmail = sharedPref.getString("email", "") ?: return@launch
                        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
                        val user = userDao.getUserByEmail(oldEmail)

                        if (user != null) {
                            val updatedUser = when (key) {
                                "username" -> user.copy(username = newValue)
                                "email" -> user.copy(email = newValue)
                                else -> user
                            }

                            userViewModel.updateUser(updatedUser)

                            // ✅ Trimitem notificare
                            val notificationMsg = when (key) {
                                "username" -> "Username-ul a fost schimbat în $newValue"
                                "email" -> "Emailul a fost schimbat în $newValue"
                                else -> ""
                            }

                            Log.d("DEBUG", "TRIMIT notificare: $notificationMsg")
                            notificationViewModel.insertNotification(
                                Notification(
                                    title = "Profil actualizat",
                                    message = notificationMsg
                                )
                            )

                            // ✅ Abia ACUM actualizăm SharedPreferences
                            sharedPref.edit {
                                putString(key, newValue)
                                apply()
                            }

                            targetTextView.text = "$label - $newValue"
                        }
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }




    private fun showChangePasswordDialog(userViewModel: UserViewModel) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_password, null)
        val oldPass = dialogView.findViewById<EditText>(R.id.editOldPassword)
        val newPass = dialogView.findViewById<EditText>(R.id.editNewPassword)

        AlertDialog.Builder(requireContext())
            .setTitle("Change Password")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val oldPassword = oldPass.text.toString().trim()
                val newPassword = newPass.text.toString().trim()

                if (oldPassword.isBlank() || newPassword.isBlank()) {
                    Toast.makeText(requireContext(), "All fields required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val email = sharedPref.getString("email", "") ?: return@setPositiveButton

                lifecycleScope.launch {
                    val userDao = AppDatabase.getDatabase(requireContext()).userDao()
                    val user = userDao.getUserByEmail(email)

                    if (user != null && user.password == HashUtils.sha256(oldPassword)) {
                        val updatedUser = user.copy(password = HashUtils.sha256(newPassword))
                        userViewModel.updateUser(updatedUser)
                        Toast.makeText(requireContext(), "Password updated!", Toast.LENGTH_SHORT).show()

                        Log.d("DEBUG", "Insert notificare: ")
                        notificationViewModel.insertNotification(
                            Notification(
                                title = "Parolă schimbată",
                                message = "Parola a fost actualizată cu succes."
                            )
                        )
                    } else {
                        Toast.makeText(requireContext(), "Old password incorrect", Toast.LENGTH_SHORT).show()
                    }
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
