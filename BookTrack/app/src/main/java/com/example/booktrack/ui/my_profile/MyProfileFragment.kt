package com.example.booktrack.ui.my_profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.booktrack.LoginActivity
import com.example.booktrack.databinding.FragmentMyProfileBinding

class MyProfileFragment : Fragment() {

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

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

        val sharedPref = requireContext().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)
        val username = sharedPref.getString("username", "N/A") ?: "N/A"
        val email = sharedPref.getString("email", "N/A") ?: "N/A"

        binding.textUsername.text = "Username - $username"
        binding.textEmail.text = "Mail - $email"

        binding.btnEditUsername.setOnClickListener {
            // poți deschide un dialog aici
        }

        binding.btnEditEmail.setOnClickListener {
            // la fel, dialog pentru editare
        }

        binding.btnChangePassword.setOnClickListener {
            // navighează sau deschide dialog pentru schimbarea parolei
        }

        // logout
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(com.example.booktrack.R.menu.profile_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    com.example.booktrack.R.id.action_logout -> {
                        val sharedPref = requireActivity().getSharedPreferences(
                            "user_prefs",
                            AppCompatActivity.MODE_PRIVATE
                        )
                       sharedPref.edit {
                            clear()
                            apply()
                        }

                        // redirect la login
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
