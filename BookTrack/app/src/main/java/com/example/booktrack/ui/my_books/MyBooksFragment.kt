package com.example.booktrack.ui.my_books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
//import androidx.navigation.fragment.R
import com.example.booktrack.R
import androidx.navigation.fragment.findNavController
import com.example.booktrack.databinding.FragmentMyBooksBinding

class MyBooksFragment : Fragment() {

private var _binding: FragmentMyBooksBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val myBooksViewModel =
            ViewModelProvider(this).get(MyBooksViewModel::class.java)

    _binding = FragmentMyBooksBinding.inflate(inflater, container, false)
    val root: View = binding.root

      val textView: TextView = binding.textMyBooks
      myBooksViewModel.text.observe(viewLifecycleOwner) {
          textView.text = it
      }


      // Seteaza click pe FloatingActionButton
      binding.floatingActionButton.setOnClickListener {
          findNavController().navigate(R.id.action_navigation_my_books_to_navigation_add_book)
      }

      return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}