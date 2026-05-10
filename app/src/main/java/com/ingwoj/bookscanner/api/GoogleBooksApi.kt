package com.ingwoj.bookscanner.api

import com.ingwoj.bookscanner.BuildConfig
import com.ingwoj.bookscanner.bookAPIData.BookResponseApi
import com.ingwoj.bookscanner.data.BookResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {

    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("printType") printType: String,
        //@Query("filter") filter: String,
        @Query("orderBy") orderBy: String,
        @Query("maxResults") maxResult: Int,
        @Query("key") key: String = BuildConfig.API_KEY
    ): Response<BookResponseApi>

    @GET("volumes")
    suspend fun searchBooksISBN(
        @Query("q") query: String,
        @Query("printType") printType: String,
        @Query("orderBy") orderBy: String,
        @Query("key") key: String = BuildConfig.API_KEY
    ): Response<BookResponseApi>
}