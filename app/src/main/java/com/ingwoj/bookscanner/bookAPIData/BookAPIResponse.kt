package com.ingwoj.bookscanner.bookAPIData

import kotlinx.serialization.Serializable

@Serializable
data class BookResponseApi(
    val kind: String, // rodzaj - books#volumes
    val totalItems: Int, // liczba odpowiedzi
    val items: List<Item>, // lista odpowiedzi
)

@Serializable
data class Item(
    val accessInfo: AccessInfo, // informacje o prawach dostępu
    val etag: String, // identyfikator wersji danych
    val id: String, // id dla googlebooks
    val kind: String, // typ zasobu - books#volume
    val selfLink: String, // link API url do poobrania szczegółów o tej książce
    val volumeInfo: VolumeInfo // dane szczegółowe książki
)
@Serializable
data class AccessInfo(
    val publicDomain: Boolean, // w sumie fajna funkcja - czy należy do domeny publicznej
)

@Serializable
data class VolumeInfo(
    val authors: List<String>, // lista autorów
    val averageRating: Double, // średnia ocen google books
    val canonicalVolumeLink: String, // link kanoniczny do strony książki (??)
    val categories: List<String>, // kategorie/gatunki
    val contentVersion: String, // wewnętrzny nymer wersji treści książki
    val description: String, // opis
    val imageLinks: ImageLinks, // linki do okładek
    val industryIdentifiers: List<IndustryIdentifier>, // ISBN
    val infoLink: String, // link do strony informacyjnej o książce
    val language: String, // język
    val maturityRating: String, // oznaczenie wiekowe
    val pageCount: Int, // liczba stron
    val previewLink: String, // link do podglądu ksiązki w przeglądarce na stornie GoogleBooks
    val printType: String, // typ publikacji BOOK MAGAZINE
    val publishedDate: String, // data wydania
    val publisher: String, // wydawca
    val ratingsCount: Int, // liczba ocen google books
    val title: String // tytuł
)

@Serializable
data class ImageLinks( // obrazki ksiażki
    val smallThumbnail: String, // mały obrazek
    val thumbnail: String // duży obrazek
)

@Serializable
data class IndustryIdentifier(
    val identifier: String, // typ identyfikatora ISBN_10 lub ISBN_13
    val type: String // znaki identyfikatora
)