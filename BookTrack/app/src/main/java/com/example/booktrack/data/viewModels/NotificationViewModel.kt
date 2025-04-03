import android.app.Application
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.booktrack.data.database.AppDatabase
import com.example.booktrack.data.models.Notification
import com.example.booktrack.data.repositories.NotificationRepository
import com.example.booktrack.data.viewModels.NotificationViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NotificationViewModel(
    application: Application,
    private val repository: NotificationRepository
) : AndroidViewModel(application) {

    val allNotifications: LiveData<List<Notification>> = repository.allNotifications

    fun insertNotification(notification: Notification) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(notification)
        }
    }
}


