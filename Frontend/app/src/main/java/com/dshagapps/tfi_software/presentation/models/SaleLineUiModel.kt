package com.dshagapps.tfi_software.presentation.models

data class SaleLineUiModel(
    val id: Int,
    val stock: StockUiModel
) {
    val subtotal: Double get() = stock.price * stock.quantity
}
