package com.ingwoj.bookscanner

class BookNotFoundException(message: String): Exception(message)
class ApiFailure(message: String): Exception(message)
class NotABarcode(message: String): Exception(message)
class BarcodeScannerFailure(message: String): Exception(message)
class NetworkError(message: String): Exception(message)