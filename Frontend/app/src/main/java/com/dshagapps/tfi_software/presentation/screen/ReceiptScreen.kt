package com.dshagapps.tfi_software.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dshagapps.tfi_software.presentation.models.ReceiptLineUiModel
import com.dshagapps.tfi_software.presentation.models.SaleLineUiModel
import com.dshagapps.tfi_software.presentation.ui.components.ScreenBottomButtons
import com.dshagapps.tfi_software.presentation.utils.formatCuit
import com.dshagapps.tfi_software.presentation.utils.receiptFormat
import com.dshagapps.tfi_software.presentation.utils.toPriceString
import com.dshagapps.tfi_software.presentation.viewmodel.SaleViewModel
import kotlinx.coroutines.launch

@Composable
fun ReceiptScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: SaleViewModel
) {
    BackHandler(onBack = onBack)

    val coroutineScope = rememberCoroutineScope()
    
    val receipt = viewModel.receipt.collectAsState().value

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        receipt?.let {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .scrollable(
                        state = rememberScrollableState(consumeScrollDelta = { 0f }),
                        orientation = Orientation.Vertical,
                        enabled = true,
                        reverseDirection = false
                    )
                    .border(BorderStroke(2.dp, Color.LightGray))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.receiptType,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Punto de venta: ${it.salePoint}",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
                Text(
                    text = "Vendedor: ${it.salesmanName}",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
                Text(
                    text = "Comp. Nro.: ${it.saleNumber}",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
                Text(
                    text = "CUIT: ${it.businessCuit.formatCuit()}",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
                Text(
                    text = "Domicilio: ${it.businessAddress}",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
                Text(
                    text = "Condición frente al IVA: ${it.businessTributeCondition}",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(BorderStroke(2.dp, Color.DarkGray))
                        .padding(8.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        text = "DETALLE:",
                        style = TextStyle(fontStyle = FontStyle.Italic)
                    )
                    it.receiptLines.forEach { line ->
                        ReceiptLineItem(
                            modifier = Modifier.padding(vertical = 4.dp),
                            line = line
                        )
                    }
                    Divider(modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(2.dp)
                        .background(Color.DarkGray))
                    Text(
                        text = "Total Neto Gravado: ${
                            it.saleTaxedAmount.toDouble().toPriceString()
                        }",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        textAlign = TextAlign.End,
                        style = TextStyle(fontStyle = FontStyle.Italic, color = Color.DarkGray)
                    )
                    Text(
                        text = "Total IVA: ${it.saleTaxAmount.toDouble().toPriceString()}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        textAlign = TextAlign.End,
                        style = TextStyle(fontStyle = FontStyle.Italic, color = Color.DarkGray)
                    )
                    Text(
                        text = "Total: ${it.saleTotalAmount.toDouble().toPriceString()}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        textAlign = TextAlign.End,
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                }
                Text(
                    text = "Nro CAE: ${it.caeNumber}",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
                Text(
                    text = "Vencimiento CAE: ${it.caeExpirationDate.receiptFormat()}",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
                Text(
                    text = "Fecha de emisión: ${it.issueDate.receiptFormat()}",
                    style = TextStyle(fontStyle = FontStyle.Italic)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        ScreenBottomButtons(
            onPrimaryButton = {
                coroutineScope.launch {
                    onBack()
                }
            }
        )
    }
}

@Composable
private fun ReceiptLineItem(
    modifier: Modifier = Modifier,
    line: ReceiptLineUiModel
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Divider(modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(2.dp)
                .background(Color.DarkGray))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Cód. Art.: ${line.code}",
                    style = TextStyle(color = Color.DarkGray)
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = line.description,
                    style = TextStyle(color = Color.DarkGray),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Cant: ${line.quantity}u",
                    style = TextStyle(color = Color.DarkGray)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = line.unitPrice.toDouble().toPriceString(),
                    style = TextStyle(color = Color.DarkGray)
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = "IVA: ${line.iva}",
                    style = TextStyle(color = Color.DarkGray),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Subtotal: ${line.lineTotal.toDouble().toPriceString()}",
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}