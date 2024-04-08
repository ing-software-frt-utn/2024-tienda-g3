package com.dshagapps.tfi_software.presentation.models

data class StockUiModel(
    val id: Int,
    val productId: Int,
    val productDescription: String,
    val sizeDescription: String,
    val colorDescription: String,
    val price: Double,
    val quantity: Int = 0,
    val maxQuantity: Int,
    val brandDescription: String,
    val categoryDescription: String
)
