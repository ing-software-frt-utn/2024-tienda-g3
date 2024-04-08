package com.dshagapps.tfi_software.data.service.schemas.responses

import java.util.Date

data class SaleResponse(
    val razonSocialEmpresa: String,
    val domicilioComercial: String,
    val empresaCondicionTributaria: String,
    val puntoDeVenta: Int,
    val nombreVendedor: String,
    val numeroVenta: Int,
    val tipoPago: String,
    val lineasDeVenta: List<SaleLineResponse>,
    val clienteCuit: String,
    val clienteCondicionTributaria: String,
    val tipoComprobante: String,
    val nroCae: String,
    val fechaVencimientoCae: String,
    val totalNetoGravado: String,
    val totalIva: String,
    val totalVenta: String,
    val fechaDeEmision: Date
)

data class SaleLineResponse(
    val id: Int,
    val codigoArticulo: Int,
    val cantidad: Int,
    val subTotal: String,
    val descripcion: String,
    val precioUnitario: String,
    val iva: String
)
