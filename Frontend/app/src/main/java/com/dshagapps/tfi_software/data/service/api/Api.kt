package com.dshagapps.tfi_software.data.service.api

import com.dshagapps.tfi_software.data.service.schemas.requests.LoginRequestBody
import com.dshagapps.tfi_software.data.service.schemas.requests.SaleRequestBody
import com.dshagapps.tfi_software.data.service.schemas.responses.BaseResponse
import com.dshagapps.tfi_software.data.service.schemas.responses.ClientResponse
import com.dshagapps.tfi_software.data.service.schemas.responses.LoginResponse
import com.dshagapps.tfi_software.data.service.schemas.responses.SaleResponse
import com.dshagapps.tfi_software.data.service.schemas.responses.StockResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @POST("api/auth/local/login")
    fun login(@Body loginBody: LoginRequestBody): Call<BaseResponse<LoginResponse>>

    @GET("api/auth/logout")
    fun logout(): Call<BaseResponse<String>>

    @GET("/api/stock/branch/{salesmenId}")
    fun getStockBySalesmen(@Path("salesmenId")salesmenId: Int): Call<BaseResponse<List<StockResponse>>>

    @GET("/api/cliente")
    fun getClientByCuit(@Query("cuit") cuit: String): Call<BaseResponse<ClientResponse>>

    @POST("/api/venta/crear-venta")
    fun startSale(
        @Body sale: SaleRequestBody,
        @Query("tipoPago") type: String
    ): Call<BaseResponse<SaleResponse>>
}