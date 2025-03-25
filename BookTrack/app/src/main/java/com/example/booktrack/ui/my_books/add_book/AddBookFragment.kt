package com.example.booktrack.ui.my_books.add_book

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.booktrack.R
import com.example.booktrack.data.database.BookDatabase
import com.example.booktrack.data.models.Book
import com.example.booktrack.data.repositories.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddBookFragment : Fragment(R.layout.fragment_add_book) {
    private lateinit var imageViewCover: ImageView
    private lateinit var buttonUploadCover: Button
    private lateinit var buttonAddBook: Button
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var spinnerGenre: Spinner
    private lateinit var editTextAuthor: EditText

    private var coverUri: Uri? = null
    private var selectedCoverResId: Int = R.drawable.hunger_games // Seteaza o coperta default


    private val bookRepository by lazy {
        BookRepository(BookDatabase.getDatabase(requireContext()).bookDao())
    }
    private val bookViewModel: AddBookViewModel by viewModels {
        AddBookViewModelFactory(bookRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        imageViewCover = view.findViewById(R.id.imageViewCover)
        buttonUploadCover = view.findViewById(R.id.buttonUploadCover)
        buttonAddBook = view.findViewById(R.id.buttonAddBook)
        editTextTitle = view.findViewById(R.id.editTextTitle)
        editTextDescription = view.findViewById(R.id.editTextDescription)
        spinnerGenre = view.findViewById(R.id.spinnerGenre)
        editTextAuthor = view.findViewById(R.id.editTextAuthor)

//        buttonUploadCover.setOnClickListener {
//            openGallery()
//        }
        spinnerGenre.setSelection(0, false)


        buttonUploadCover.setOnClickListener {
            showCoverSelectionDialog()
        }

        buttonAddBook.setOnClickListener {
            saveBookToDatabase()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            coverUri = data?.data
            imageViewCover.setImageURI(coverUri)
        }
    }

    private fun saveBookToDatabase() {
        val title = editTextTitle.text.toString().trim()
        val description = editTextDescription.text.toString().trim()
        val genre = spinnerGenre.selectedItem.toString()
        val author = editTextAuthor.text.toString().trim()

        if (title.isEmpty() || description.isEmpty() || author.isEmpty()) {
            Toast.makeText(requireContext(), "Complete all fields!", Toast.LENGTH_SHORT).show()
            return
        }

        val book = Book(
            title = title,
            description = description,
            genre = genre,
            author = author,
            coverImageResId = selectedCoverResId
        )

        bookViewModel.insertBook(book)

        Toast.makeText(requireContext(), "The book has been added!", Toast.LENGTH_SHORT).show()
//        requireActivity().onBackPressed()
        findNavController().popBackStack(R.id.navigation_my_books, false)
    }

    private fun showCoverSelectionDialog() {
        val coverImages = listOf(R.drawable.hunger_games, R.drawable.hunger_games_2, R.drawable.hunger_games_3) // AICI TREBUIE SA PUN COPERTILE, de modificat !!!
        val coverNames = arrayOf("Cover 1", "Cover 2", "Cover 3") // AICI TREBUIE SA PUN COPERTILE, de modificat !!!

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose a cover")
        builder.setItems(coverNames) { _, which ->
            selectedCoverResId = coverImages[which] // salveaza ID-ul imaginii selectate
            imageViewCover.setImageResource(selectedCoverResId) // seteaza imaginea in ImageView
        }
        builder.show()
    }


    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }
}
