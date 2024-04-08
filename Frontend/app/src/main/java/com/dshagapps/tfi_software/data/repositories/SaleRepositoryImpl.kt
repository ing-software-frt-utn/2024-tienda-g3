package com.dshagapps.tfi_software.data.repositories

import android.util.Log
import com.dshagapps.tfi_software.data.service.adapters.ErrorResponseDeserializer.deserialize
import com.dshagapps.tfi_software.data.service.api.Api
import com.dshagapps.tfi_software.data.service.schemas.requests.LoginRequestBody
import com.dshagapps.tfi_software.data.utils.toClient
import com.dshagapps.tfi_software.data.utils.toReceipt
import com.dshagapps.tfi_software.data.utils.toRequestBody
import com.dshagapps.tfi_software.data.utils.toStock
import com.dshagapps.tfi_software.data.utils.toUser
import com.dshagapps.tfi_software.domain.entities.Client
import com.dshagapps.tfi_software.domain.entities.Receipt
import com.dshagapps.tfi_software.domain.entities.Sale
import com.dshagapps.tfi_software.domain.entities.Stock
import com.dshagapps.tfi_software.domain.entities.User
import com.dshagapps.tfi_software.domain.enums.PaymentType
import com.dshagapps.tfi_software.domain.repositories.SaleRepository

class SaleRepositoryImpl(
    private val api: Api
): SaleRepository {
    override suspend fun login(user: String, password: String): Result<User> {
        api.login(LoginRequestBody(user, password)).execute().also {  response ->
            return if (response.isSuccessful) {
                Log.d("RESPONSE", response.body().toString())
                Result.success(
                    response.body()!!.content.toUser()
                )
            } else {
                Result.failure(
                    Exception(deserialize(response.errorBody()).message)
                )
            }
        }
    }

    override suspend fun logout(): Result<String> {
        api.logout().execute().also { response ->
            return if (response.isSuccessful) {
                Result.success(response.body()!!.content)
            } else {
                Result.failure(
                    Exception(deserialize(response.errorBody()).message)
                )
            }
        }
    }

    override suspend fun getStockBySalesmenId(salesmenId: Int): Result<List<Stock>> {
        api.getStockBySalesmen(salesmenId).execute().also { response ->
            return if (response.isSuccessful) {
                Result.success(
                    response.body()!!.content.map { stockResponse -> stockResponse.toStock() }
                )
            } else {
                Result.failure(
                    Exception(deserialize(response.errorBody()).message)
                )
            }
        }
    }

    override suspend fun getClientByCuit(cuit: String): Result<Client> {
        api.getClientByCuit(cuit).execute().also { response ->
            return if (response.isSuccessful) {
                Result.success(response.body()!!.content.toClient())
            } else {
                Result.failure(
                    Exception(deserialize(response.errorBody()).message)
                )
            }
        }
    }

    override suspend fun startSale(sale: Sale, type: PaymentType): Result<Receipt> {
        val paymentType = when (type) {
            PaymentType.CASH -> "EFECTIVO"
            PaymentType.CARD -> "TARJETA"
        }
        api.startSale(sale.toRequestBody(), paymentType).execute().also { response ->
            return if (response.isSuccessful) {
                Result.success(response.body()!!.content.toReceipt())
            } else {
                Result.failure(
                    Exception(deserialize(response.errorBody()).message)
                )
            }
        }
    }
}