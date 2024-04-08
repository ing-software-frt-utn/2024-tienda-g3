package com.dshagapps.tfi_software.presentation.utils

import com.dshagapps.tfi_software.domain.entities.SaleLine
import com.dshagapps.tfi_software.presentation.models.ClientUiModel
import com.dshagapps.tfi_software.presentation.models.SaleLineUiModel

fun SaleLineUiModel.incrementStockQuantity(): SaleLineUiModel =
    this.copy(stock = this.stock.copy(quantity = minOf(this.stock.quantity + 1, this.stock.maxQuantity)))

fun SaleLineUiModel.decrementStockQuantity(): SaleLineUiModel =
    this.copy(stock = this.stock.copy(quantity = maxOf(this.stock.quantity - 1, 0)))

fun List<SaleLineUiModel>.getTotal(): Double =
    this.sumOf { line -> line.subtotal }

fun List<SaleLineUiModel>.toDomainEntity(): List<SaleLine> =
    this.map { line -> SaleLine(stockId = line.stock.id, quantity = line.stock.quantity) }
fun ClientUiModel.fullName(): String = "${this.firstName} ${this.lastName}"