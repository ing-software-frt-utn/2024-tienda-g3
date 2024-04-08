package com.dshagapps.tfi_software.presentation.models

import com.dshagapps.tfi_software.domain.entities.Client
import com.dshagapps.tfi_software.domain.enums.TributeCondition
import com.dshagapps.tfi_software.presentation.utils.toUiModel

data class ClientUiModel(
    val firstName: String,
    val lastName: String,
    val address: String,
    val cuit: String,
    val tributeCondition: TributeCondition
) {
    fun isAnonymousClient(): Boolean =
        this == anonymousClient
    companion object {
        val anonymousClient = Client.anonymousClient.toUiModel()
    }
}
