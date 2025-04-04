package com.example.booktrack.ui.my_books.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booktrack.data.models.ReviewWithUser
import com.example.booktrack.databinding.ItemReviewBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BookReviewsAdapter : RecyclerView.Adapter<BookReviewsAdapter.ReviewViewHolder>() {

    private var reviews = listOf<ReviewWithUser>()

    fun submitList(list: List<ReviewWithUser>) {
        reviews = list
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReviewWithUser) {
            binding.textViewUsername.text = item.user.username
            binding.textViewReviewText.text = item.review.reviewText
            binding.textViewRating.text = "⭐ ${item.review.rating}"
            binding.textViewDate.text = formatDate(item.review.reviewDate)
        }

        private fun formatDate(timestamp: Long): String {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount() = reviews.size

}