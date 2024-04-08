package com.dshagapps.tfi_software.data.service.schemas.responses

data class BaseResponse<T> (
    val content: T,
    val error: ErrorResponse
)

data class ErrorResponse(
    val message: String
)