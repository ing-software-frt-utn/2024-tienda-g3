package com.dshagapps.tfi_software.data.service.schemas.requests

data class SaleRequestBody(
    val lineasDeVenta: List<SaleLineRequestBody>,
    val tarjeta: CardRequestBody?,
    val monto: String,
    val clienteCuit: String,
)

data class CardRequestBody(
    val titular: String,
    val numero: String,
    val mesVencimiento: String,
    val anioVencimiento: String,
    val ccv: String
)

data class SaleLineRequestBody(
    val stockId: Int,
    val cantidad: Int
)
