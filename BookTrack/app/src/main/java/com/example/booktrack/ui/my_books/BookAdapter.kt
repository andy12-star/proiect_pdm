package com.example.booktrack.ui.my_books

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.booktrack.R
import com.example.booktrack.data.models.Book
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide

class BookAdapter(
    private val onItemClick: (Book) -> Unit
) : ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {


    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textViewTitle)
        val author: TextView = itemView.findViewById(R.id.textViewAuthor)
        val cover: ImageView = itemView.findViewById(R.id.imageViewCover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {


        val currentBook = getItem(position)

        holder.itemView.setOnClickListener {
            onItemClick(currentBook)
        }


        holder.title.text = currentBook.title
        holder.author.text = currentBook.author

        val context = holder.itemView.context

        val imageResId = context.resources.getIdentifier(currentBook.coverImageFileName.split(".")[0], "drawable", context.packageName)


        if (imageResId != 0) {
            // folosim Glide pentru a incarca imaginea din 'drawable'
            Glide.with(context)
                .load(imageResId)
                .into(holder.cover)
        } else {
            // daca imaginea nu exista, seteaza o imagine default
            Glide.with(context)
                .load(R.drawable.ic_launcher_foreground)
                .into(holder.cover)
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    // clasa de callback pentru diferentierea intre iteme în lista
    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id // sa ma asigur ca am un 'id' unic pt fiecare carte
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }
}