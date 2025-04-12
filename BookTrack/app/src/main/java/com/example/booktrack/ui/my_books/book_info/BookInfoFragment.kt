package com.example.booktrack.ui.my_books.book_info

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.booktrack.R
import com.example.booktrack.databinding.FragmentBookInfoBinding
import androidx.navigation.fragment.findNavController
import com.example.booktrack.data.dao.BookDao
import com.example.booktrack.data.dao.ReviewDao
import com.example.booktrack.data.database.AppDatabase
import com.example.booktrack.data.models.UserBook
import com.example.booktrack.data.repositories.UserRepository
import com.example.booktrack.data.viewModels.UserViewModel
import com.example.booktrack.data.viewModels.UserViewModelFactory
import com.example.booktrack.ui.my_books.user_book.UserBookViewModel
import kotlinx.coroutines.launch
import android.util.Log

class BookInfoFragment : Fragment() {

    private var _binding: FragmentBookInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var title: String
    private lateinit var author: String
    private lateinit var description: String
    private lateinit var coverImageFileName: String

    private lateinit var bookDao: BookDao
    private lateinit var reviewDao: ReviewDao

    private var bookId: Int = 0
    private var userId: Int = 0

    private lateinit var userViewModel: UserViewModel
    lateinit var userBookViewModel: UserBookViewModel

//    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val args = BookInfoFragmentArgs.fromBundle(it)
            title = args.title
            author = args.author
            description = args.description
            coverImageFileName = args.coverImageFileName
        }

        bookDao = AppDatabase.getDatabase(requireContext()).bookDao()
        reviewDao = AppDatabase.getDatabase(requireContext()).reviewDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookInfoBinding.inflate(inflater, container, false)

        // obtine User ID dintr-un mecanism de autentificare:
        userId = getUserIdFromAuthSystem()
        Log.e("BookInfoFragmenttt", userId?.toString() ?: "User ID is null")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val userDao = AppDatabase.getDatabase(requireContext()).userDao()
        val userRepository = UserRepository(userDao)
        val factory = UserViewModelFactory(requireActivity().application, userRepository)

        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        userBookViewModel = ViewModelProvider(this).get(UserBookViewModel::class.java)


        binding.textViewTitle.text = title
        binding.textViewAuthor.text = author
        binding.textViewDescription.text = description


        val imageName = coverImageFileName.split(".")[0]
        val imageResId = requireContext().resources.getIdentifier(
            imageName, "drawable", requireContext().packageName
        )

        if (imageResId != 0) {
            Glide.with(requireContext())
                .load(imageResId)
                .into(binding.imageViewCover)
        } else {
            Glide.with(requireContext())
                .load(R.drawable.ic_launcher_foreground)
                .into(binding.imageViewCover)
        }

        // folosim coroutine pentru a accesa baza de date pe un thread de fundal (pt ca am suspend in Dao):
        lifecycleScope.launch {
            try {
                bookId = bookDao.getBookIdByTitle(title) ?: return@launch

                Log.e("BookInfoFragment", bookId?.toString() ?: "Book ID is null")
                val userBook = userBookViewModel.getUserBook(userId, bookId)
                userBook?.let {
                    val shelfText = it.bookshelf.replaceFirstChar { c -> c.uppercase() }
                    binding.btnAddBookToBookshelf.text = "$shelfText"
                }

                val ratings = reviewDao.getRatingsForBook(bookId)
                val averageRating = calculateAverageRating(ratings)
                // afiseaza media ratingurilor:
                binding.textViewRating.text = "⭐ $averageRating"

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.btnAddBookToBookshelf.setOnClickListener {

            if (userId != null && bookId != null) {
                showBookshelfSelectionDialog(
                    userId = userId,
                    bookId = bookId,
                    button = binding.btnAddBookToBookshelf
                )
            } else {
                Toast.makeText(requireContext(), "User not available", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAddReview.setOnClickListener {
            // navigheaza la CreateReviewFragment:
            val action = BookInfoFragmentDirections.actionNavigationBookInfoToNavigationCreateReview(title)
            findNavController().navigate(action)
        }

        binding.btnSeeBookReviews.setOnClickListener {
            val action = BookInfoFragmentDirections.actionNavigationBookInfoToNavigationBookReviews(title)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun calculateAverageRating(ratings: List<Float>): Float {
        return if (ratings.isNotEmpty()) {
            val total = ratings.sum()
            val average = total / ratings.size
            // rotunjire la 2 zecimale:
            String.format("%.2f", average).toFloat()
        } else {
            0f  // daca nu sunt ratinguri, returneaza 0
        }
    }

    private fun showBookshelfSelectionDialog(userId: Int, bookId: Int, button: Button) {
        val bookshelfNames = arrayOf("To-Read", "Reading", "Read")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose a bookshelf")
        builder.setItems(bookshelfNames) { _, which ->

            val selectedBookshelf = bookshelfNames[which]

            val userBook = UserBook(
                userId = userId,
                bookId = bookId,
                bookshelf = selectedBookshelf.lowercase(),
                dateAdded = System.currentTimeMillis()
            )

            // inseram in DB:
            lifecycleScope.launch {
                userBookViewModel.insert(userBook)

                // actualizam textul butonului:
                button.text = "$selectedBookshelf"
            }

        }
        builder.show()
    }

    private fun getUserIdFromAuthSystem(): Int {
        val sharedPref = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)
        return userId
    }
}