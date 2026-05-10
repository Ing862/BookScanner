package com.ingwoj.bookscanner.search_menu

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingwoj.bookscanner.ApiFailure
import com.ingwoj.bookscanner.BookNotFoundException
import com.ingwoj.bookscanner.book.BookUiState
import com.ingwoj.bookscanner.data.Book
import com.ingwoj.bookscanner.data.BookResponse
import com.ingwoj.bookscanner.repository.BooksRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel(
    private val repository: BooksRepository = BooksRepository
): ViewModel() {

//    private val _openBook = MutableSharedFlow<String>()
//    val openBook = _openBook

    private val _events = MutableSharedFlow<SearchEvent>()
    val events = _events.asSharedFlow()

    private val _state = MutableStateFlow<BookUiState>(BookUiState.Idle)
    val state: StateFlow<BookUiState> = _state

    private val _bookList = MutableLiveData<List<Book>>()
    val bookList: LiveData<List<Book>> = _bookList

    /**
     * Deals with the scanned isbn
     * @param isbn ISBN of scanned book
     */
    fun onScan(isbn: String) {
        Log.d("SearchVM", "onScan: $isbn")
        viewModelScope.launch {
            _events.emit(SearchEvent.OpenBook(isbn))
        }
    }

    fun resetToIdle() {
        _state.value = BookUiState.Idle
    }

    sealed class SearchEvent {
        data class OpenBook(val isbn: String): SearchEvent()
    }

    var searchJob: Job? = null

    fun onQueryChanged(query: String) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(300)

            val result = repository.searchBooksByTitleOrAuthor(query,20)

            result.onSuccess { bookResponse ->
                _bookList.value = bookResponse.items ?: emptyList()
            }
                .onFailure {
                    _bookList.value = emptyList()
                }
        }
    }

    fun onBookClicked(book: Book) {
        Log.d("BOOK DETAILS ", "Szczegóły książki")
    }

}