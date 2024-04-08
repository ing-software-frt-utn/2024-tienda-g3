package com.dshagapps.tfi_software.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshagapps.tfi_software.domain.entities.Card
import com.dshagapps.tfi_software.domain.entities.Sale
import com.dshagapps.tfi_software.domain.enums.PaymentType
import com.dshagapps.tfi_software.domain.repositories.SaleRepository
import com.dshagapps.tfi_software.domain.rules.AnonymousClientRules
import com.dshagapps.tfi_software.presentation.models.ClientUiModel
import com.dshagapps.tfi_software.presentation.models.ReceiptUiModel
import com.dshagapps.tfi_software.presentation.models.SaleLineUiModel
import com.dshagapps.tfi_software.presentation.models.StockUiModel
import com.dshagapps.tfi_software.presentation.utils.decrementStockQuantity
import com.dshagapps.tfi_software.presentation.utils.formatWithoutDecimalSeparator
import com.dshagapps.tfi_software.presentation.utils.getTotal
import com.dshagapps.tfi_software.presentation.utils.incrementStockQuantity
import com.dshagapps.tfi_software.presentation.utils.toDomainEntity
import com.dshagapps.tfi_software.presentation.utils.toUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SaleViewModel(
    private val repository: SaleRepository
) : ViewModel() {

    private var salesmenId: Int? = null

    val saleLines: MutableStateFlow<List<SaleLineUiModel>> = MutableStateFlow(emptyList())

    val receipt: MutableStateFlow<ReceiptUiModel?> = MutableStateFlow(null)

    fun login(
        username: String,
        password: String,
        onSuccessCallback: () -> Unit,
        onFailureCallback: (Throwable) -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {
        repository.login(username, password).fold(
            onSuccess = {
                salesmenId = it.salesmenId
                onSuccessCallback()
            },
            onFailure = onFailureCallback
        )
    }

    fun logout(
        onSuccessCallback: (String) -> Unit,
        onFailureCallback: (Throwable) -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {
        repository.logout().fold(
            onSuccess = onSuccessCallback,
            onFailure = onFailureCallback
        )
    }

    fun getStock(
        onFailureCallback: (Throwable) -> Unit
    ): Flow<List<StockUiModel>> = flow {
        salesmenId?.let {
            repository.getStockBySalesmenId(it).fold(
                onSuccess = { stockList ->
                    emit(stockList.map { stock -> stock.toUiModel() })
                },
                onFailure = onFailureCallback
            )
        } ?: onFailureCallback(Exception("Error obteniendo id del vendedor"))
    }.flowOn(Dispatchers.IO)

    fun getClientByCuit(
        cuit: String,
        onFailureCallback: (Throwable) -> Unit
    ): Flow<ClientUiModel> = flow {
        repository.getClientByCuit(cuit).fold(
            onSuccess = { client ->
                emit(client.toUiModel())
            },
            onFailure = onFailureCallback
        )
    }.flowOn(Dispatchers.IO)

    fun startSale(
        cardNumber: String,
        cardHolder: String,
        cardExpiryMonth: String,
        cardExpiryYear: String,
        cardCcv: String,
        clientCuit: String,
        onSuccessCallback: () -> Unit,
        onFailureCallback: (Throwable) -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {
        repository.startSale(
            sale = Sale(
                saleLines = saleLines.value.toDomainEntity(),
                card = Card(
                    number = cardNumber,
                    holder = cardHolder,
                    expiryMonth = cardExpiryMonth,
                    expiryYear = cardExpiryYear,
                    ccv = cardCcv
                ),
                amount = saleLines.value.getTotal().formatWithoutDecimalSeparator(),
                clientCuit = clientCuit
            ),
            type = PaymentType.CARD
        ).fold(
            onSuccess = { newReceipt ->
                receipt.value = newReceipt.toUiModel()
                onSuccessCallback()
            },
            onFailure = onFailureCallback
        )
    }
    fun startSale(
        clientCuit: String,
        onSuccessCallback: () -> Unit,
        onFailureCallback: (Throwable) -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {
        repository.startSale(
            Sale(
                saleLines = saleLines.value.toDomainEntity(),
                card = null,
                amount = saleLines.value.getTotal().formatWithoutDecimalSeparator(),
                clientCuit = clientCuit
            ),
            type = PaymentType.CASH
        ).fold(
            onSuccess = { newReceipt ->
                receipt.value = newReceipt.toUiModel()
                onSuccessCallback()
            },
            onFailure = onFailureCallback
        )
    }


    fun addProductToSale(stock: StockUiModel) {
        val existingSaleLine = saleLines.value.find { line -> line.stock.id == stock.id }

        if (existingSaleLine == null) {
            val newSaleLine = SaleLineUiModel(
                id = saleLines.value.size + 1,
                stock = stock.copy(quantity = 1),
            )
            saleLines.value = saleLines.value + newSaleLine
        } else {
            val updatedSaleLines = saleLines.value.map { line ->
                if (line.id == existingSaleLine.id) {
                    line.incrementStockQuantity()
                } else {
                    line
                }
            }
            saleLines.value = updatedSaleLines
        }
    }

    fun removeProductFromSale(stock: StockUiModel) {
        val existingSaleLine = saleLines.value.find { line -> line.stock.id == stock.id }

        if (existingSaleLine != null) {
            val updatedSaleLines = saleLines.value.map { line ->
                if (line.id == existingSaleLine.id) {
                    line.decrementStockQuantity()
                } else {
                    line
                }
            }
            saleLines.value = updatedSaleLines.filterNot { line -> line.stock.quantity == 0 }
        }
    }

    val isNominalClient: Boolean
        get() = AnonymousClientRules.isNominalClient(saleLines.value.getTotal())


    fun removeSaleLineById(lineId: Int) {
        saleLines.value = saleLines.value.filterNot { line -> line.id == lineId }
    }

    fun cleanStates() {
        saleLines.value = emptyList()
        receipt.value = null
    }
}