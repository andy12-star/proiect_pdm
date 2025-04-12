package com.example.booktrack.data.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.booktrack.data.models.User
import com.example.booktrack.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application, private val userRepository: UserRepository): AndroidViewModel(application) {
val readAllData: LiveData<List<User>> = userRepository.readAllData
    private val _currentUser = MutableLiveData<User?>()
//    val currentUser: LiveData<User?> = _currentUser

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.addUser(user)
        }
    }

    // Metoda pentru a obtine utilizatorul curent:
//    fun loadCurrentUser(userId: Int) {
//        viewModelScope.launch {
//            _currentUser.value = userRepository.getUserById(userId)
//
//        }
//    }

    suspend fun login(email: String, password: String): User? {
        return userRepository.login(email, password)
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }

}