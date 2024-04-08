package com.dshagapps.tfi_software.presentation.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.dshagapps.tfi_software.presentation.models.ClientUiModel
import com.dshagapps.tfi_software.presentation.ui.components.Loader
import com.dshagapps.tfi_software.presentation.ui.components.ScreenBottomButtons
import com.dshagapps.tfi_software.presentation.utils.fullName
import com.dshagapps.tfi_software.presentation.utils.getTotal
import com.dshagapps.tfi_software.presentation.utils.toPriceString
import com.dshagapps.tfi_software.presentation.viewmodel.SaleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onContinue: (clientName: String, clientCuit: String) -> Unit,
    onSale: () -> Unit,
    viewModel: SaleViewModel,
) {
    BackHandler(onBack = onBack)

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    var loading by remember { mutableStateOf(false) }

    var client by remember { mutableStateOf(ClientUiModel.anonymousClient) }
    var isAnonymousClientChecked by remember { mutableStateOf(viewModel.isNominalClient.not()) }
    var cuit by remember { mutableStateOf("") }

    val isAnonymousClientCheckboxEnabled = viewModel.isNominalClient.not()

    var isCardPaymentChecked by remember { mutableStateOf(false) }

    val isPrimaryButtonEnabled = (isAnonymousClientChecked || client.isAnonymousClient().not()) && loading.not()

    if (loading) {
        Loader()
    } else {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LabeledCheckbox(
                modifier = Modifier.fillMaxWidth(),
                label = "Es cliente anónimo",
                checked = if (isAnonymousClientCheckboxEnabled) isAnonymousClientChecked else false,
                onCheckedChange = {
                    isAnonymousClientChecked = !isAnonymousClientChecked
                    if (isAnonymousClientChecked) isCardPaymentChecked = false
                },
                enabled = isAnonymousClientCheckboxEnabled
            )

            LabeledCheckbox(
                modifier = Modifier.fillMaxWidth(),
                label = "Pago con tarjeta",
                checked = isCardPaymentChecked,
                onCheckedChange = {
                    isCardPaymentChecked = !isCardPaymentChecked
                    if (isCardPaymentChecked) isAnonymousClientChecked = false
                }
            )

            if (!isAnonymousClientChecked) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1.0f),
                        value = cuit,
                        onValueChange = { cuit = it },
                        maxLines = 1,
                        label = {
                            Text("CUIT")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        )
                    )

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.getClientByCuit(cuit) {
                                    this.launch {
                                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }.collect { client = it }
                            }
                        }
                    ) {
                        Text("Buscar Cliente")
                    }
                }

                if (client.isAnonymousClient().not()) {
                    ClientCard(
                        modifier = Modifier.fillMaxWidth(),
                        clientUiModel = client
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
                Text(
                    text = "Total: ${viewModel.saleLines.value.getTotal().toPriceString()}",
                    fontSize = 7.em,
                    fontWeight = FontWeight.Bold
                )
            }

            ScreenBottomButtons(
                onPrimaryButton = {
                    loading = true
                    if (isCardPaymentChecked) {
                        onContinue(
                            client.fullName(),
                            client.cuit
                        )
                    } else {
                        viewModel.startSale(
                            clientCuit = client.cuit,
                            onSuccessCallback = {
                                coroutineScope.launch(Dispatchers.Main) {
                                    onSale()
                                }
                            },
                            onFailureCallback = {
                                loading = false
                                coroutineScope.launch {
                                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                },
                onSecondaryButton = onBack,
                primaryButtonEnabled = isPrimaryButtonEnabled
            )
        }
    }
}


@Composable
private fun LabeledCheckbox(
    modifier: Modifier = Modifier,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
        Text(
            text = label,
            style = TextStyle(
                color = if (enabled) Color.Unspecified else Color.LightGray
            )
        )
    }
}

@Composable
private fun ClientCard(
    modifier: Modifier = Modifier,
    clientUiModel: ClientUiModel
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = "Nombre: ${clientUiModel.firstName}")
            Text(text = "Apellido: ${clientUiModel.lastName}")
            Text(text = "Domicilio: ${clientUiModel.address}")
            Text(text = "CUIT: ${clientUiModel.cuit}")
            Text(text = "Condición Tributaria: ${clientUiModel.tributeCondition}")
        }
    }
}