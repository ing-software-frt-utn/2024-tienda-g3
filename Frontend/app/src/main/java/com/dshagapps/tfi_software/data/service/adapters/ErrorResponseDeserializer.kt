package com.dshagapps.tfi_software.data.service.adapters

import com.dshagapps.tfi_software.data.service.schemas.responses.ErrorResponse
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.ResponseBody
import java.lang.reflect.Type

object ErrorResponseDeserializer {
    fun deserialize(responseBody: ResponseBody?): ErrorResponse {
        val gson = GsonBuilder().registerTypeAdapter(
            ErrorResponse::class.java, object : JsonDeserializer<ErrorResponse> {
                override fun deserialize(
                    json: JsonElement?,
                    typeOfT: Type?,
                    context: JsonDeserializationContext?
                ): ErrorResponse {
                    val jsonObject = json?.asJsonObject
                    val errorMessage = jsonObject?.get("message")?.asString

                    return ErrorResponse(errorMessage ?: "Unknown message")
                }
            }
        ).create()

        return gson.fromJson(responseBody?.charStream(), ErrorResponse::class.java)
    }
}
