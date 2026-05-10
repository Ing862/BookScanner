package com.ingwoj.bookscanner.data

data class BookResponse(
    val totalItems: Int,
    val items: List<Book>
)

data class Book (
    val id: String?,
    val isbn: List<String> = emptyList(),
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val categories: List<String>?,
    val publicDomain: Boolean?,
    val averageRating: Double?,
    val ratingsCount: Int?,
    val language: String?,
    val maturityRating: String?,
    val pageCount: Int?,
    val imageSmall: String?,
    val image: String?
)