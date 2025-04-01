package com.example.booktrack.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktrack.data.database.AppDatabase
import com.example.booktrack.databinding.FragmentGenreBooksBinding
import com.example.booktrack.ui.my_books.BookAdapter
import kotlinx.coroutines.launch

class GenreBooksFragment : Fragment() {

    private var _binding: FragmentGenreBooksBinding? = null
    private val binding get() = _binding!!

    private lateinit var genre: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            genre = GenreBooksFragmentArgs.fromBundle(it).genre
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenreBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setăm titlul în Toolbar
        (requireActivity() as AppCompatActivity).supportActionBar?.title = genre

        binding.recyclerViewBooks.layoutManager = LinearLayoutManager(requireContext())

        val bookDao = AppDatabase.getDatabase(requireContext()).bookDao()
        lifecycleScope.launch {
            val books = bookDao.getBooksByGenre(genre)
            binding.recyclerViewBooks.adapter = BookAdapter { selectedBook ->
                val action = GenreBooksFragmentDirections
                    .actionGenreBooksFragmentToNavigationBookInfo(
                        title = selectedBook.title,
                        author = selectedBook.author,
                        description = selectedBook.description,
                        coverImageFileName = selectedBook.coverImageFileName // ✅
                    )

                findNavController().navigate(action)
            }.apply {
                submitList(books)
            }

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
