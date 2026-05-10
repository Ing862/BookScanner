//package com.ingawoj.novela.book
//
//import androidx.fragment.app.viewModels
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.lifecycleScope
//import androidx.lifecycle.repeatOnLifecycle
//import com.google.android.material.snackbar.Snackbar
//import com.ingawoj.novela.databinding.FragmentBookDetailsBinding
//import com.ingawoj.novela.features.books.data.Book
//import kotlinx.coroutines.launch
//import coil.load
//
//class BookDetailsFragment : Fragment() {
//
//    // TODO: co to jest?
//    companion object {
//        private const val ARG_ISBN = "isbn"
//        fun newInstance(isbn: String) = BookDetailsFragment().apply {
//            arguments = Bundle().apply {
//                putString(ARG_ISBN, isbn)
//            }
//        }
//    }
//
//    private val viewModel: BookDetailsViewModel by viewModels()
//    private var _binding: FragmentBookDetailsBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentBookDetailsBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
//        super.onViewCreated(view, savedInstanceState)
//
//        changeLoadingVisibility(false)
//        bookNotFoundMess(false)
//
//        val isbn = requireArguments().getString("isbn")!!
//
//        Log.d("API", "Szukam książki po ISBN: $isbn")
//        viewModel.loadBook(isbn)
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.state.collect { state ->
//                    when (state) {
//                        is BookUiState.Loading -> {
//                            changeLoadingVisibility(true)
//                        } // TODO: loading zium
//                        is BookUiState.Success -> {
//                            if (state.book == null) {
//                                // TODO: pokaż nie znaleziono książki
//                                bookNotFoundMess(true)
//                            }
//                            else {
//                                showBook(state.book)
//                            }
//                            changeLoadingVisibility(false)
//
//                        } // TODO: funkcja wyświetlająca
//                        is BookUiState.Error -> {
//                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
//                            changeLoadingVisibility(false)
//
//                        } // TODO: popup z wiadomością
//                        else -> changeLoadingVisibility(false)
//                    }
//                }
//            }
//        }
//
//    }
//
//
//    fun showBook(book: Book){
//        binding.bookTitle.text = book.title
//
//        // TODO: recyclerView dla wyświetlania wielu autorów
//        //binding.bookAuthor.text = book.authors.joinToString(separator = ", ") {it. name}
//
//        binding.bookDescription.text = book.description
//
//        // TODO: Url cover
//        binding.bookCover.load(book.image){
//            crossfade(true)
////            placeholder(R.drawable.book_cover_placeholder) // TODO: placeholder i error obrazek
////            error(R.drawable.book_cover_placeholder)
//        }
//
//        // TODO: API rating
//
//        // TODO: App rating
//
//        // TODO: Categories
//
//        // TODO: public domain
//    }
//
//    fun changeLoadingVisibility(showLoading: Boolean) {
//        if (showLoading) {
//            binding.bookLayout.visibility = View.INVISIBLE
//            binding.loadingScreen.visibility = View.VISIBLE
//        }
//        else {
//            binding.bookLayout.visibility = View.VISIBLE
//            binding.loadingScreen.visibility = View.INVISIBLE
//        }
//    }
//
//    fun bookNotFoundMess(show: Boolean) {
//        if (show) {
//            binding.bookLayout.visibility = View.INVISIBLE
//            binding.bookNotFoundScreen.visibility = View.VISIBLE
//        }
//        else {
//            binding.bookLayout.visibility = View.VISIBLE
//            binding.bookNotFoundScreen.visibility = View.INVISIBLE
//        }
//    }
//}