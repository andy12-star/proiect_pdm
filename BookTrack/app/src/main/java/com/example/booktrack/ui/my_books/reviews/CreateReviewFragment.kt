package com.example.booktrack.ui.my_books.reviews

import NotificationViewModel
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.booktrack.data.models.Review
import com.example.booktrack.data.repositories.ReviewRepository
import com.example.booktrack.databinding.FragmentCreateReviewBinding
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.booktrack.data.dao.ReviewDao
import com.example.booktrack.data.database.AppDatabase
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.example.booktrack.data.models.Notification
import com.example.booktrack.data.repositories.NotificationRepository
import com.example.booktrack.data.viewModels.NotificationViewModelFactory
import kotlinx.coroutines.launch

class CreateReviewFragment : Fragment() {

    private var _binding: FragmentCreateReviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var reviewRepository: ReviewRepository
    private lateinit var notificationViewModel: NotificationViewModel


    private var bookId: Int = 0 // Book ID primit de la BookInfoFragment
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateReviewBinding.inflate(inflater, container, false)

        val db = AppDatabase.getDatabase(requireContext())

        val notificationDao = db.notificationDao()
        val notificationRepo = NotificationRepository(notificationDao)
        val notificationFactory = NotificationViewModelFactory(requireActivity().application, notificationRepo)
        notificationViewModel = ViewModelProvider(requireActivity(), notificationFactory)[NotificationViewModel::class.java]


        // obtine DAO-ul din baza de date
        val reviewDao = db.reviewDao()
        val bookDao = db.bookDao()
        val userDao = db.userDao()

        // initializeaza ReviewRepository cu ReviewDao
        reviewRepository = ReviewRepository(reviewDao, bookDao, userDao)

        // obtine User ID dintr-un mecanism de autentificare
        userId = getUserIdFromAuthSystem()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reviewEditText: EditText = binding.editTextReviewText
        val ratingBar: RatingBar = binding.ratingBar
        val submitButton: Button = binding.btnSubmitReview

        submitButton.setOnClickListener {
            val reviewText = reviewEditText.text.toString()
            val rating = ratingBar.rating

            if (reviewText.isNotEmpty() && rating > 0) {
                lifecycleScope.launch {
                    val bookTitle = arguments?.getString("bookTitle")
                    if (bookTitle.isNullOrEmpty()) {
                        Log.e("CreateReviewFragment", "bookTitle is missing from arguments")
                        Toast.makeText(requireContext(), "Error: Book title missing!", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    bookId = getBookIdFromDatabase(bookTitle) // asteapta rezultatul

                    val bookExists = reviewRepository.isBookExist(bookId)
                    val userExists = reviewRepository.isUserExist(userId)

                    if (bookExists && userExists) {
                        val review = Review(
                            bookId = bookId,
                            userId = userId,
                            rating = rating,
                            reviewText = reviewText,
                            reviewDate = System.currentTimeMillis()
                        )

                        reviewRepository.insertReview(review)

                        notificationViewModel.insertNotification(
                            Notification(
                                title = "Review adăugat",
                                message = "Ai adăugat un review pentru cartea \"$bookTitle\""
                            )
                        )

                        Toast.makeText(requireContext(), "Review submitted: $reviewText", Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressed()
                    } else {
                        Toast.makeText(requireContext(), "Invalid book or user", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please enter a review and select a rating", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getUserIdFromAuthSystem(): Int {
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)
        return userId
    }


    private suspend fun getBookIdFromDatabase(bookTitle: String): Int {
        val db = AppDatabase.getDatabase(requireContext())
        val bookDao = db.bookDao()

        val book = bookDao.getBookByTitle(bookTitle)

        val bookId = book?.id ?: 0
        return bookId

    }

}