package com.ingwoj.bookscanner.book

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.ingwoj.bookscanner.R
import com.ingwoj.bookscanner.data.Book
import com.ingwoj.bookscanner.databinding.ActivityBookDetailsBinding
import kotlinx.coroutines.launch

class BookDetailsActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context, isbn: String): Intent {
            return Intent(context, BookDetailsActivity::class.java).apply {
                putExtra("isbn", isbn)
            }
        }
    }

    private val viewModel: BookDetailsViewModel by viewModels()
    private var _binding: ActivityBookDetailsBinding ?= null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        _binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeLoadingVisibility(false)
        bookNotFoundMess(false)

        val isbn = intent.getStringExtra("isbn") ?: error("No ISBN")

        Log.d("API", "Szukam książki po ISBN: $isbn")
        viewModel.loadBookByIsbn(isbn)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is BookUiState.Loading -> {
                            changeLoadingVisibility(true)
                        } // TODO: loading zium
                        is BookUiState.Success -> {
                            if (state.book == null) {
                                Log.d("BOOK DETAILS", "Nic tu nie ma do pokazania")
                                bookNotFoundMess(true) // TODO: test
                            }
                            else {
                                Log.d("BOOK DETAILS", "Showing:${state.book.title}")
                                showBook(state.book) // TODO: test
                            }
                            changeLoadingVisibility(false)

                        } // TODO: funkcja wyświetlająca
                        is BookUiState.Error -> {
                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                            changeLoadingVisibility(false)

                        } // TODO: popup z wiadomością
                        else -> changeLoadingVisibility(false)
                    }
                }
            }
        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    fun showBook(book: Book){
        Log.d("SHOW BOOK FUN", "Wyświetlam książkę: ${book.title}")
        binding.bookTitle.text = book.title

        // TODO: recyclerView dla wyświetlania wielu autorów
        binding.bookAuthor.text = if (book.authors.isNullOrEmpty()) "Brak autorów"
            else book.authors.joinToString(separator = ", ")

        if (book.description != null) binding.bookDescription.text = book.description

        // TODO: Url cover
        val safeHttp = if (book.image != null && book.image.startsWith("http://")) book.image.replace("http://", "https://")
        else book.image
        binding.bookCover.load(safeHttp){
            placeholder(R.drawable.placeholder_image)
            error(R.drawable.placeholder_image)
        }

        // TODO: API rating in google books

        // TODO: App rating in app

        // TODO: Categories
//        val layoutManager = FlexboxLayoutManager(this)
//        layoutManager.flexDirection = FlexDirection.ROW
//        layoutManager.flexWrap = FlexWrap.WRAP

//        binding.categoriesRecyclerView.layoutManager = layoutManager

        // TODO: public domain
    }

    fun changeLoadingVisibility(showLoading: Boolean) {
        if (showLoading) {
            binding.bookLayout.visibility = View.INVISIBLE
            binding.loadingScreen.visibility = View.VISIBLE
        }
        else {
            binding.bookLayout.visibility = View.VISIBLE
            binding.loadingScreen.visibility = View.INVISIBLE
        }
    }

    fun bookNotFoundMess(show: Boolean) {
        if (show) {
            binding.bookLayout.visibility = View.INVISIBLE
            binding.bookNotFoundScreen.visibility = View.VISIBLE
        }
        else {
            binding.bookLayout.visibility = View.VISIBLE
            binding.bookNotFoundScreen.visibility = View.INVISIBLE
        }
    }
}