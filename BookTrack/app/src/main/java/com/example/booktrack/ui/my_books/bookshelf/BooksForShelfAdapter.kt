package com.example.booktrack.ui.my_books.bookshelf

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booktrack.R
import com.example.booktrack.data.models.Book
import com.example.booktrack.databinding.ItemBookBinding
import java.util.Locale


class BooksForShelfAdapter : ListAdapter<Book, BooksForShelfAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    class BookViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val imageViewCover: ImageView = view.findViewById(R.id.imageViewCover)
        private val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        private val textViewAuthor: TextView = view.findViewById(R.id.textViewAuthor)

        fun bind(book: Book) {
            // seteaza datele cartii in componentele UI:
            textViewTitle.text = book.title
            textViewAuthor.text = book.author

            val context = view.context
            Log.d("BookAdapter", "Cover image name: ${book.coverImageFileName}")

            fun Context.getDrawableIdByName(fileName: String): Int {
                val cleanName = fileName.substringBeforeLast((".")) // elimina extensia daca exista (adica .png)
                return this.resources.getIdentifier(cleanName, "drawable", this.packageName)
            }


            val imageResId = context.getDrawableIdByName(book.coverImageFileName)
            if (imageResId != 0) {
                imageViewCover.setImageResource(imageResId)
            }

        }

    }

    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }
}

