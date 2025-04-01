package com.example.booktrack.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.booktrack.R
import com.example.booktrack.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

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
//
//        val bookDao = AppDatabase.getDatabase(requireContext()).bookDao()

        binding.recyclerViewGenres.layoutManager = GridLayoutManager(requireContext(), 2)

        lifecycleScope.launch {
            val genres = resources.getStringArray(R.array.book_genres).toList()
                .drop(1)
            binding.recyclerViewGenres.adapter = GenreAdapter(genres) { selectedGenre ->
                val action = SearchFragmentDirections
                    .actionSearchFragmentToGenreBooksFragment(selectedGenre)
                findNavController().navigate(action)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
