package com.example.booktrack.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktrack.data.database.AppDatabase
import com.example.booktrack.databinding.FragmentSearchBinding
import com.example.booktrack.ui.my_books.BookAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookAdapter: BookAdapter
    private lateinit var genreAdapter: GenreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookDao = AppDatabase.getDatabase(requireContext()).bookDao()

        bookAdapter = BookAdapter()
        genreAdapter = GenreAdapter(
            genres = resources.getStringArray(com.example.booktrack.R.array.book_genres).toList().drop(1)
        ) { selectedGenre ->
            val action = SearchFragmentDirections
                .actionSearchFragmentToGenreBooksFragment(selectedGenre)
            findNavController().navigate(action)
        }

        binding.recyclerViewGenres.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewGenres.adapter = genreAdapter

        binding.recyclerViewBooks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewBooks.adapter = bookAdapter

        binding.recyclerViewGenres.visibility = View.VISIBLE
        binding.recyclerViewBooks.visibility = View.GONE


        lifecycleScope.launch {
            val allBooks = bookDao.getAllBooks().first()

            bookAdapter.submitList(allBooks)

            binding.searchBar.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {

                    val query = binding.searchBar.text.toString().trim()

                    val filteredBooks = allBooks.filter { book ->
                        book.title.contains(query, ignoreCase = true) || book.author.contains(query, ignoreCase = true)
                    }

                    bookAdapter.submitList(filteredBooks)

                    // Afișăm doar lista de cărți
                    binding.recyclerViewBooks.visibility = View.VISIBLE
                    binding.recyclerViewGenres.visibility = View.GONE

                    true
                } else {
                    false
                }
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
