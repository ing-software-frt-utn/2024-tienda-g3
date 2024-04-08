package com.dshagapps.tfi_software.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.dshagapps.tfi_software.presentation.models.SaleLineUiModel
import com.dshagapps.tfi_software.presentation.ui.components.ScreenBottomButtons
import com.dshagapps.tfi_software.presentation.utils.getTotal
import com.dshagapps.tfi_software.presentation.utils.toPriceString
import com.dshagapps.tfi_software.presentation.viewmodel.SaleViewModel

@Composable
fun SaleLinesDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    viewModel: SaleViewModel
) {
    BackHandler(onBack = onBack)

    val saleLines = viewModel.saleLines.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items(saleLines.value) { line ->
                SaleLineItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    line = line,
                    onDeleteSaleLine = viewModel::removeSaleLineById
                )
            }
        }

        Spacer(modifier = modifier.weight(1.0f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text (
                text = "Total: ${saleLines.value.getTotal().toPriceString()}",
                fontSize = 7.em,
                fontWeight = FontWeight.Bold
            )
        }

        ScreenBottomButtons(
            modifier = Modifier.fillMaxWidth(),
            onPrimaryButton = onContinue,
            onSecondaryButton = onBack
        )
    }

    LaunchedEffect(saleLines.value) {
        if (saleLines.value.isEmpty()) {
            onBack()
        }
    }
}

@Composable
private fun SaleLineItem(
    modifier: Modifier = Modifier,
    line: SaleLineUiModel,
    onDeleteSaleLine: (lineId: Int) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Button(
            onClick = {
                onDeleteSaleLine(line.id)
            }
        ) {
            Text(text = "X")
        }
        Column(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    modifier = Modifier.weight(1.0f),
                    text = "${line.stock.productDescription} ${line.stock.colorDescription} ${line.stock.sizeDescription} x ${line.stock.quantity}u.",
                    style = TextStyle(color = Color.DarkGray)
                )
                Text(
                    text = "Precio unitario: ${line.stock.price.toPriceString()}",
                    style = TextStyle(color = Color.DarkGray)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                Text(
                    text = "Subtotal: ${line.subtotal.toPriceString()}",
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            }
            Divider(modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(2.dp)
                .background(Color.DarkGray))
        }
    }
}