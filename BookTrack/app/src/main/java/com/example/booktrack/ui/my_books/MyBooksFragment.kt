package com.example.booktrack.ui.my_books

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.booktrack.R
import androidx.navigation.fragment.findNavController
import com.example.booktrack.data.database.AppDatabase
import com.example.booktrack.data.repositories.BookRepository
import com.example.booktrack.databinding.FragmentMyBooksBinding
import androidx.recyclerview.widget.LinearLayoutManager


class MyBooksFragment : Fragment() {

    private var _binding: FragmentMyBooksBinding? = null
    private val binding get() = _binding!!

    private lateinit var myBooksViewModel: MyBooksViewModel
    private lateinit var bookAdapter: BookAdapter

    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bookRepository = BookRepository(AppDatabase.getDatabase(requireContext()).bookDao())
        val viewModelFactory = MyBooksViewModelFactory(bookRepository)
        myBooksViewModel = ViewModelProvider(this, viewModelFactory).get(MyBooksViewModel::class.java)

        _binding = FragmentMyBooksBinding.inflate(inflater, container, false)

        userId = getUserIdFromAuthSystem()

        val root: View = binding.root



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        // seteaza adapterul pentru RecyclerView:
        bookAdapter = BookAdapter { selectedBook ->
            val action = MyBooksFragmentDirections
                .actionNavigationMyBooksToNavigationBookInfo(
                    title = selectedBook.title,
                    author = selectedBook.author,
                    description = selectedBook.description,
                    coverImageFileName = selectedBook.coverImageFileName // adaugat
                )

            findNavController().navigate(action)
        }


        binding.recyclerViewBooks.layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerViewBooks.adapter = bookAdapter

        // observa LiveData pentru actualizarea listei de carti
        myBooksViewModel.allBooks.observe(viewLifecycleOwner) { books ->

            bookAdapter.submitList(books.toList())
//            bookAdapter.notifyDataSetChanged()
        }

        // seteaza click pe FloatingActionButton
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_my_books_to_navigation_add_book)
        }

        binding.btnToRead.setOnClickListener {
            navigateToShelf("to-read")
        }

        binding.btnReading.setOnClickListener {
            navigateToShelf("reading")
        }

        binding.btnRead.setOnClickListener {
            navigateToShelf("read")
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

    private fun navigateToShelf(bookshelf: String) {
        val action = MyBooksFragmentDirections.actionNavigationMyBooksToNavigationMyBooksShelf(bookshelf, userId)
        findNavController().navigate(action)
    }

}