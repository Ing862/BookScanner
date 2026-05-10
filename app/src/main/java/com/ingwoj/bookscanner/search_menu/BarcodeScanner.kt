package com.ingwoj.bookscanner.search_menu

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

val options = GmsBarcodeScannerOptions.Builder()
    .setBarcodeFormats(Barcode.FORMAT_EAN_13)
    .build()

fun scanBarcode(context: Context, onResult: (String) -> Unit, onError: (String) -> Unit){
    val scanner = GmsBarcodeScanning.getClient(context, options)

    scanner.startScan()
        .addOnSuccessListener { barcode ->
            Toast.makeText(context, "u did it!", Toast.LENGTH_SHORT).show()
            // deal with barecode
            val rawValue = barcode.rawValue
            if (rawValue != null && validateBarcode(rawValue)) {
                onResult(rawValue)
                // TODO: Obsłużyć kod
                Log.d("SKANER", "Zeskanowany kod: $rawValue") // log

            }
            else {
                onError("Invalid barcode")
            }
        }
        .addOnFailureListener {
            onError("Invalid barcode")
        }
}

fun validateBarcode(code: String): Boolean {
    return code.length == 13 && (code.startsWith("978") || code.startsWith("979"))
}