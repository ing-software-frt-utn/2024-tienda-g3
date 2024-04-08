package com.dshagapps.tfi_software.domain.rules

object AnonymousClientRules {
    fun isNominalClient(amount: Double): Boolean = amount >= NOMINAL_CLIENT_FLOOR_AMOUNT

    private const val NOMINAL_CLIENT_FLOOR_AMOUNT = 19162400
}
