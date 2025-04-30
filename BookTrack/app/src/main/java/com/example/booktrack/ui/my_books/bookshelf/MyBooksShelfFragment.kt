package com.example.booktrack.ui.my_books.bookshelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.booktrack.R
import com.example.booktrack.data.dao.UserBookDao
import com.example.booktrack.data.database.AppDatabase
import com.example.booktrack.data.models.Book
import com.example.booktrack.data.models.ReviewWithUser
import kotlinx.coroutines.launch

class MyBooksShelfFragment : Fragment() {

    private lateinit var booksForShelfAdapter: BooksForShelfAdapter
    private lateinit var booksForShelfViewModel: BooksForShelfViewModel
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_books_shelves, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recuperez bookshelf si userId din argumente:
        val args = MyBooksShelfFragmentArgs.fromBundle(requireArguments())
        val bookshelf = args.bookshelf
        userId = args.userId

        if (userId == -1 || userId == 0) {
            Toast.makeText(context, "User ID invalid!", Toast.LENGTH_SHORT).show()
            return
        }

        // configurez RecyclerView:
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewBookshelf)
        recyclerView.layoutManager = LinearLayoutManager(context)

        booksForShelfAdapter = BooksForShelfAdapter()
        recyclerView.adapter = booksForShelfAdapter

        // obtin ViewModel si observ datele pentru shelf-ul respectiv si user:
        booksForShelfViewModel = ViewModelProvider(this).get(BooksForShelfViewModel::class.java)

        booksForShelfViewModel.getBooksForShelf(userId, bookshelf).observe(viewLifecycleOwner) { books ->
            books?.let {
                booksForShelfAdapter.submitList(it)
            }
        }

    }

}
