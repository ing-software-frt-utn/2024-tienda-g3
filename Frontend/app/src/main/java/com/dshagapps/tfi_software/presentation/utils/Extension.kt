package com.dshagapps.tfi_software.presentation.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date

fun Double.toPriceString(): String = DecimalFormat.getCurrencyInstance().format(this / 100)

fun Double.formatWithoutDecimalSeparator(): String {
    val formattedValue = String.format("%.2f", this)
    return formattedValue.replace(".", "")
}

fun Date.receiptFormat(): String = SimpleDateFormat("dd/MM/yyyy").format(this)

fun String.formatCuit(): String = this.substring(0,2) + '-' + this.substring(2, this.length - 2) + '-' + this.substring(length - 1)