package com.dshagapps.tfi_software.data.service.schemas.responses

data class ClientResponse(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val domicilio: String,
    val CUIT: String,
    val condicionTributariaId: Int,
    val condicionTributaria: String
)
