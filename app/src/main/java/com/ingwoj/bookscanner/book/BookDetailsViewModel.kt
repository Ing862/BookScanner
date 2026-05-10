package com.ingwoj.bookscanner.book

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingwoj.bookscanner.repository.BooksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookDetailsViewModel() : ViewModel() {
    // TODO: Implement the ViewModel

    private val repository = BooksRepository
    private val _state = MutableStateFlow<BookUiState>(BookUiState.Idle)
    val state: StateFlow<BookUiState> = _state

    fun loadBookByIsbn(isbn: String) {
        viewModelScope.launch {
            _state.value = BookUiState.Loading

            Log.d("API", "przed szukaniem")

            val result = repository.searchBooksByISBN(isbn, 1) // FIXME: Null w wyszukiwaniu
            Log.d("API", "po szukaniu")

            result.onSuccess { data ->
                Log.d("API_DEBUG", "items: ${data.items}")
                Log.d("API_DEBUG", "items size: ${data.items?.size}")
                val book = data.items.firstOrNull()
                _state.value = BookUiState.Success(data, book)
                Log.d("API_BOOK", "znalazło książkę: $isbn")
            }
            result.onFailure { error ->
                _state.value = BookUiState.Error(error.message ?: "Unknown error")
                Log.d("API", "error")
            }
        }
    }

}