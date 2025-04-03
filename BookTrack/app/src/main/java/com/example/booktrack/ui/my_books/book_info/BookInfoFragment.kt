package com.example.booktrack.ui.my_books.book_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.booktrack.R
import com.example.booktrack.databinding.FragmentBookInfoBinding
import androidx.navigation.fragment.findNavController

class BookInfoFragment : Fragment() {

    private var _binding: FragmentBookInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var title: String
    private lateinit var author: String
    private lateinit var description: String
    private lateinit var coverImageFileName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val args = BookInfoFragmentArgs.fromBundle(it)
            title = args.title
            author = args.author
            description = args.description
            coverImageFileName = args.coverImageFileName
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewTitle.text = title
        binding.textViewAuthor.text = author
        binding.textViewDescription.text = description

        val imageName = coverImageFileName.split(".")[0]
        val imageResId = requireContext().resources.getIdentifier(
            imageName, "drawable", requireContext().packageName
        )

        if (imageResId != 0) {
            Glide.with(requireContext())
                .load(imageResId)
                .into(binding.imageViewCover)
        } else {
            Glide.with(requireContext())
                .load(R.drawable.ic_launcher_foreground)
                .into(binding.imageViewCover)
        }

        binding.btnAddReview.setOnClickListener {
            // navigheaza la CreateReviewFragment
            val action = BookInfoFragmentDirections.actionNavigationBookInfoToNavigationCreateReview(title)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}