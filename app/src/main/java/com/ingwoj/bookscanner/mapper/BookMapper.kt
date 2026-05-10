package com.ingwoj.bookscanner.data.mapper

import com.ingwoj.bookscanner.bookAPIData.BookResponseApi
import com.ingwoj.bookscanner.bookAPIData.Item
import com.ingwoj.bookscanner.data.Book
import com.ingwoj.bookscanner.data.BookResponse

fun convertIsbn(isbn: String): String{
    if (isbn.length == 13) return isbn
    else {
        var newIsbn = isbn.removeRange(12,13)
        newIsbn = "978$newIsbn"
        var sum = 0
        for (i in newIsbn.indices) {
            val digit = i.toString().toInt()
            sum += if (i % 2 == 1) digit
            else digit * 3
        }
        val check = (10 - (sum % 10)) % 10

        return newIsbn + check
    }
}
fun BookResponseApi.toBookResponse(): BookResponse {
    return BookResponse(
        totalItems = totalItems,
        items = items?.map { it.toBook()} ?: emptyList()
    )
}

fun Item.toBook(): Book {
    return Book (
        id = id,
        isbn = volumeInfo.industryIdentifiers?.map {it.identifier} ?: emptyList(),
        title = volumeInfo.title,
        authors = volumeInfo.authors ?: emptyList(),
        description = volumeInfo.description,
        categories = volumeInfo.categories ?: emptyList(),
        publicDomain = accessInfo.publicDomain,
        averageRating = volumeInfo.averageRating,
        ratingsCount = volumeInfo.ratingsCount,
        language = volumeInfo.language,
        maturityRating = volumeInfo.maturityRating,
        pageCount = volumeInfo.pageCount,
        imageSmall = volumeInfo.imageLinks?.smallThumbnail,
        image = volumeInfo.imageLinks?.thumbnail
    )
}