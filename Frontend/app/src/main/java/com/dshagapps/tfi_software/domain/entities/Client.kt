package com.dshagapps.tfi_software.domain.entities

import com.dshagapps.tfi_software.domain.enums.TributeCondition

data class Client(
    val firstName: String,
    val lastName: String,
    val address: String,
    val cuit: String,
    val tributeCondition: TributeCondition
) {
    companion object {
        val anonymousClient = Client(
            firstName = "",
            lastName = "",
            address = "",
            cuit = "99999999999",
            tributeCondition = TributeCondition.CONSUMIDOR_FINAL
        )
    }
}