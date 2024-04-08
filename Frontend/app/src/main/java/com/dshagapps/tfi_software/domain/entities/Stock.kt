package com.dshagapps.tfi_software.domain.entities

data class Stock(
    val id: Int,
    val productId: Int,
    val sizeId: Int,
    val colorId: Int,
    val productDescription: String,
    val sizeDescription: String,
    val colorDescription: String,
    val price: Double,
    val quantity: Int,
    val brandDescription: String,
    val categoryDescription: String
)
