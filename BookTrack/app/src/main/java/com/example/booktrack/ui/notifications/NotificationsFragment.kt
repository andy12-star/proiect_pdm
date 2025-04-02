package com.example.booktrack.ui.notifications

import NotificationViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktrack.data.database.AppDatabase
import com.example.booktrack.data.repositories.NotificationRepository
import com.example.booktrack.data.viewModels.NotificationViewModelFactory
import com.example.booktrack.databinding.FragmentNotificationsBinding
import com.example.booktrack.ui.notifications.adapter.NotificationAdapter

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NotificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = AppDatabase.getDatabase(requireContext()).notificationDao()
        val repository = NotificationRepository(dao)
        val factory = NotificationViewModelFactory(requireActivity().application, repository)
        viewModel = ViewModelProvider(this, factory)[NotificationViewModel::class.java]

        binding.recyclerViewNotifications.layoutManager = LinearLayoutManager(requireContext())

        viewModel.allNotifications.observe(viewLifecycleOwner) { notifications ->
            binding.recyclerViewNotifications.adapter = NotificationAdapter(notifications)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
