package com.example.booktrack.ui.my_books.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktrack.data.database.AppDatabase
import com.example.booktrack.data.models.ReviewWithUser
import com.example.booktrack.databinding.FragmentBookReviewsBinding
import kotlinx.coroutines.launch

class BookReviewsFragment : Fragment() {

    private var _binding: FragmentBookReviewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookReviewsAdapter: BookReviewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookTitle = BookReviewsFragmentArgs.fromBundle(requireArguments()).bookTitle

        bookReviewsAdapter = BookReviewsAdapter()
        binding.recyclerViewReviews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bookReviewsAdapter
        }

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val book = db.bookDao().getBookByTitle(bookTitle)

            book?.let {
                val reviewsWithUsers: List<ReviewWithUser> =
                    db.reviewDao().getReviewsWithUsers(it.id)
                bookReviewsAdapter.submitList(reviewsWithUsers)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}