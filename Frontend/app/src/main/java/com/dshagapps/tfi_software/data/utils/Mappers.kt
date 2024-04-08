package com.dshagapps.tfi_software.data.utils

import com.dshagapps.tfi_software.data.service.schemas.requests.CardRequestBody
import com.dshagapps.tfi_software.data.service.schemas.requests.SaleLineRequestBody
import com.dshagapps.tfi_software.data.service.schemas.requests.SaleRequestBody
import com.dshagapps.tfi_software.data.service.schemas.responses.ClientResponse
import com.dshagapps.tfi_software.data.service.schemas.responses.LoginResponse
import com.dshagapps.tfi_software.data.service.schemas.responses.SaleLineResponse
import com.dshagapps.tfi_software.data.service.schemas.responses.SaleResponse
import com.dshagapps.tfi_software.data.service.schemas.responses.StockResponse
import com.dshagapps.tfi_software.domain.entities.Card
import com.dshagapps.tfi_software.domain.entities.Client
import com.dshagapps.tfi_software.domain.entities.Receipt
import com.dshagapps.tfi_software.domain.entities.ReceiptLine
import com.dshagapps.tfi_software.domain.entities.Sale
import com.dshagapps.tfi_software.domain.entities.SaleLine
import com.dshagapps.tfi_software.domain.entities.Stock
import com.dshagapps.tfi_software.domain.entities.User
import com.dshagapps.tfi_software.domain.enums.PaymentType
import com.dshagapps.tfi_software.domain.enums.TributeCondition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun StockResponse.toStock(): Stock =
    Stock(
        id = id,
        productId = articuloId,
        sizeId = talleId,
        colorId = colorId,
        productDescription = articulo,
        sizeDescription = talle,
        colorDescription = color,
        price = precio.toDoubleOrNull() ?: 0.0,
        quantity = cantidad,
        brandDescription = marca,
        categoryDescription = categoria
    )

internal fun ClientResponse.toClient(): Client =
    Client(
        firstName = nombre,
        lastName = apellido,
        address = domicilio,
        cuit = CUIT,
        tributeCondition = TributeCondition.fromValue(condicionTributariaId)
    )

internal fun LoginResponse.toUser(): User =
    User(
        userId = id,
        username = username,
        salesmenId = vendedorId
    )

internal fun SaleResponse.toReceipt(): Receipt =
    Receipt(
        razonSocialEmpresa,
        domicilioComercial,
        empresaCondicionTributaria.toTributeCondition(),
        puntoDeVenta,
        nombreVendedor,
        numeroVenta,
        tipoPago.toPaymentType(),
        lineasDeVenta.toReceiptLines(),
        clienteCuit,
        clienteCondicionTributaria.toTributeCondition(),
        tipoComprobante,
        nroCae,
        fechaVencimientoCae.toCaeExpirationDate(),
        totalNetoGravado,
        totalIva,
        totalVenta,
        fechaDeEmision
    )

internal fun SaleLineResponse.toReceiptLine(): ReceiptLine =
    ReceiptLine(
        id,
        codigoArticulo,
        cantidad,
        subTotal,
        descripcion,
        precioUnitario,
        iva
    )

internal fun List<SaleLineResponse>.toReceiptLines(): List<ReceiptLine> =
    this.map { line -> line.toReceiptLine() }

internal fun Sale.toRequestBody(): SaleRequestBody =
    SaleRequestBody(
        lineasDeVenta = saleLines.toRequestBody(),
        tarjeta = card?.toRequestBody(),
        monto = amount,
        clienteCuit = clientCuit
    )

private fun Card.toRequestBody(): CardRequestBody =
    CardRequestBody(
        titular = holder,
        numero = number,
        mesVencimiento = expiryMonth,
        anioVencimiento = expiryYear,
        ccv = ccv
    )

private fun List<SaleLine>.toRequestBody(): List<SaleLineRequestBody> =
    this.map { line -> SaleLineRequestBody(stockId = line.stockId, cantidad = line.quantity) }


private fun String.toPaymentType(): PaymentType =
    when (this) {
        "TARJETA" -> PaymentType.CARD
        else -> PaymentType.CASH
    }

private fun String.toTributeCondition(): TributeCondition =
    when (this) {
        "RESPONSABLE INSCRIPTO" -> TributeCondition.RESPONSABLE_INSCRIPTO
        "MONOTRIBUTO" -> TributeCondition.MONOTRIBUTO
        "NO RESPONSABLE" -> TributeCondition.NO_RESPONSABLE
        "EXENTO" -> TributeCondition.EXENTO
        else -> TributeCondition.CONSUMIDOR_FINAL
    }

private fun String.toCaeExpirationDate(): Date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(this) as Date
