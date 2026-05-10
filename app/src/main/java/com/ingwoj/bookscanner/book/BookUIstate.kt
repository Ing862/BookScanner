package com.ingwoj.bookscanner.book

import com.ingwoj.bookscanner.data.Book
import com.ingwoj.bookscanner.data.BookResponse

sealed class BookUiState {
    object Idle: BookUiState()
    object Loading: BookUiState()
    data class Success(
        val response: BookResponse,
        val book: Book?
    ): BookUiState()
    data class Error(
        val message: String
    ): BookUiState()
}