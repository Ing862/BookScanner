package com.ingwoj.bookscanner.repository

import android.util.Log
import com.ingwoj.bookscanner.ApiFailure
import com.ingwoj.bookscanner.BuildConfig
import com.ingwoj.bookscanner.api.GoogleBooksApi
import com.ingwoj.bookscanner.data.mapper.toBookResponse
import com.ingwoj.bookscanner.data.BookResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BooksRepository {

    private val api: GoogleBooksApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(GoogleBooksApi::class.java)
    }

    /**
     * Searches number of books with certain isbn
     * @param isbn isbn of the book
     * @param number number of books function needs to return
     * @return list of books meeting the requirements
     */
    suspend fun searchBooksByISBN(isbn: String, number: Int): Result<BookResponse> {
        Log.d("searchBooksRepo", "Szukam książki po ISBN: $isbn") // log

        val response = api.searchBooksISBN("isbn:$isbn", QuerySettings.PRINT_TYPE, QuerySettings.ORDER_BY, BuildConfig.API_KEY)

        if (!response.isSuccessful) return Result.failure(ApiFailure("HTTP code: ${response.code()}"))

        val body = response.body()

        if (body == null) {
            Log.d("searchBooksRepo", "body is null")
            return Result.failure(ApiFailure("Empty body"))
        }

        val bookResponse = body.toBookResponse()

        return Result.success(bookResponse)
    }

    // TODO: dokończyć po naprawieniu wyszukiwania po skanowaniu
    suspend fun searchBooksByTitleOrAuthor(search: String, number: Int): Result<BookResponse> {
        Log.d("searchBooksRepo", "szukam książki po tytule lub autorze")

//        val response = api.searchBooks(search, QuerySettings.PRINT_TYPE, QuerySettings.FILTER,
//            QuerySettings.ORDER_BY, 40, BuildConfig.API_KEY)

        val response = api.searchBooks(search, QuerySettings.PRINT_TYPE,
            QuerySettings.ORDER_BY, 40, BuildConfig.API_KEY)

        if (!response.isSuccessful) return Result.failure(ApiFailure("HTTP code: ${response.code()}"))
        val body = response.body()

        if (body == null) {
            Log.d("searchBooksRepo", "body is null")
            return Result.failure(ApiFailure("Empty body"))
        }

        val bookResponse = body.toBookResponse()

        val filteredItems = bookResponse.items
            ?.filter { !it.authors.isNullOrEmpty() }
            ?.filter { !it.title.isNullOrEmpty() }
            ?.filter { !it.isbn.isNullOrEmpty() }
            ?: emptyList()

        return Result.success(
            bookResponse.copy(items = filteredItems)
        )

        //return Result.success(bookResponse)
    }

    object QuerySettings {
        const val ORDER_BY = "relevance"
        const val PRINT_TYPE = "books"
        const val FILTER = "ebooks"

    }
}