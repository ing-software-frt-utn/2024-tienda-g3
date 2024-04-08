package com.dshagapps.tfi_software.domain.entities

data class Sale(
    val saleLines: List<SaleLine>,
    val card: Card?,
    val amount: String,
    val clientCuit: String
)

data class Card(
    val number: String,
    val holder: String,
    val expiryMonth: String,
    val expiryYear: String,
    val ccv: String
)

data class SaleLine(
    val stockId: Int,
    val quantity: Int
)