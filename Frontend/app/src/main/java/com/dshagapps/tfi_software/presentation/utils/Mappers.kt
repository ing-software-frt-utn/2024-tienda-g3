package com.dshagapps.tfi_software.presentation.utils

import com.dshagapps.tfi_software.domain.entities.Client
import com.dshagapps.tfi_software.domain.entities.Receipt
import com.dshagapps.tfi_software.domain.entities.ReceiptLine
import com.dshagapps.tfi_software.domain.entities.Stock
import com.dshagapps.tfi_software.presentation.models.ClientUiModel
import com.dshagapps.tfi_software.presentation.models.ReceiptLineUiModel
import com.dshagapps.tfi_software.presentation.models.ReceiptUiModel
import com.dshagapps.tfi_software.presentation.models.StockUiModel

fun Stock.toUiModel(): StockUiModel =
    StockUiModel(
        id = id,
        productId = productId,
        productDescription = productDescription,
        sizeDescription = sizeDescription,
        colorDescription = colorDescription,
        price = price,
        maxQuantity = quantity,
        brandDescription = brandDescription,
        categoryDescription = categoryDescription
    )

fun Client.toUiModel(): ClientUiModel =
    ClientUiModel(
        firstName,
        lastName,
        address,
        cuit,
        tributeCondition
    )

fun Receipt.toUiModel(): ReceiptUiModel =
    ReceiptUiModel(
        businessCuit = businessCuit,
        businessAddress = businessAddress,
        businessTributeCondition = businessTributeCondition,
        salePoint = salePoint,
        salesmanName = salesmanName,
        saleNumber = saleNumber,
        paymentType = paymentType,
        receiptLines = receiptLines.toUiModels(),
        customerCuit = customerCuit,
        customerTributeCondition = customerTributeCondition,
        receiptType = receiptType,
        caeNumber = caeNumber,
        caeExpirationDate = caeExpirationDate,
        saleTaxedAmount = saleTaxedAmount,
        saleTaxAmount = saleTaxAmount,
        saleTotalAmount = saleTotalAmount,
        issueDate = issueDate
    )

fun List<ReceiptLine>.toUiModels(): List<ReceiptLineUiModel> =
    this.map { line ->
        ReceiptLineUiModel(
            id = line.id,
            code = line.code,
            quantity = line.quantity,
            lineTotal = line.lineTotal,
            description = line.description,
            unitPrice = line.unitPrice,
            iva = line.iva
        )
    }