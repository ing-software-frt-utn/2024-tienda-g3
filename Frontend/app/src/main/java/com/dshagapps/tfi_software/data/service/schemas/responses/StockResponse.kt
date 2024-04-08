package com.dshagapps.tfi_software.data.service.schemas.responses

data class StockResponse(
    val id: Int,
    val cantidad: Int,
    val color: String,
    val talle: String,
    val articulo: String,
    val colorId: Int,
    val articuloId: Int,
    val talleId: Int,
    val precio: String,
    val marca: String,
    val categoria: String
)
