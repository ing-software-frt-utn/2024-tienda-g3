package com.dshagapps.tfi_software.presentation.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.dshagapps.tfi_software.presentation.models.StockUiModel
import com.dshagapps.tfi_software.presentation.ui.components.ScreenBottomButtons
import com.dshagapps.tfi_software.presentation.utils.toPriceString
import com.dshagapps.tfi_software.presentation.viewmodel.SaleViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDetailsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    viewModel: SaleViewModel
) {
    BackHandler(onBack = onBack)

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val saleLines = viewModel.saleLines.collectAsState()

    var search by remember { mutableStateOf("") }

    var stockList: List<StockUiModel> by remember { mutableStateOf(emptyList()) }

    val filteredStockList: List<StockUiModel> by remember {
        derivedStateOf {
            stockList.filter { stock ->
                search.split(" ").any {
                    keyword ->
                        stock.productDescription.contains(keyword, ignoreCase = true)
                        || stock.colorDescription.contains(keyword, ignoreCase = true)
                        || stock.sizeDescription.contains(keyword, ignoreCase = true)
                        || keyword.toIntOrNull()?.equals(stock.productId) == true
                }
            }.map { stock ->
                saleLines.value.find { line -> line.stock.id == stock.id }?.stock ?: stock.copy(quantity = 0)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = search,
            onValueChange = { search = it },
            maxLines = 1,
            label = { Text("Buscar") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
        )

        LazyColumn(
            modifier = Modifier
                .weight(1.0f)
                .padding(vertical = 8.dp)
        ) {
            this.items(filteredStockList) { stock ->
                StockCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    stock = stock,
                    onAddProduct = {
                        viewModel.addProductToSale(stock)
                    },
                    onRemoveProduct = {
                        viewModel.removeProductFromSale(stock)
                    }
                )
            }
        }

        ScreenBottomButtons(
            modifier = Modifier.fillMaxWidth(),
            onPrimaryButton = onContinue,
            onSecondaryButton = onBack,
            primaryButtonEnabled = saleLines.value.isNotEmpty()
        )
    }

    LaunchedEffect(Unit) {
        viewModel.getStock(
            onFailureCallback = {
                this.launch {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }.invokeOnCompletion {
                    onBack()
                }
            }
        ).collect { stock ->
            stockList = stock
        }
    }
}

@Composable
private fun StockCard(
    modifier: Modifier = Modifier,
    stock: StockUiModel,
    onAddProduct: () -> Unit,
    onRemoveProduct: () -> Unit
) {
    Card(
        modifier = modifier
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StyledText(
                    title = "Prenda:",
                    description = stock.productDescription
                )
                StyledText(
                    title = "Marca:",
                    description = stock.brandDescription
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StyledText(
                    title = "Color:",
                    description = stock.colorDescription
                )
                StyledText(
                    title = "Talle:",
                    description = stock.sizeDescription
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StyledText(
                    title = "Precio:",
                    description = stock.price.toPriceString()
                )

                StyledText(
                    title = "${stock.quantity}:",
                    description = "${stock.maxQuantity}"
                )
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onAddProduct,
                    enabled = stock.quantity < stock.maxQuantity
                ) {
                    Text(text = "Agregar a la venta")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onRemoveProduct,
                    enabled = stock.quantity > 0
                ) {
                    Text(text = "Quitar de la venta" )
                }
            }
        }
    }
}


@Composable
private fun StyledText(
    modifier: Modifier = Modifier,
    title: String,
    description: String
) {

    val combinedText = buildAnnotatedString {
        append(title)
        append(" ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(description)
        }
    }

    Text(
        modifier = modifier,
        text = combinedText
    )
}