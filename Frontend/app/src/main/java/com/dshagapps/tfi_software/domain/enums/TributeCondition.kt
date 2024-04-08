package com.dshagapps.tfi_software.domain.enums

enum class TributeCondition(val value: Int) {
    RESPONSABLE_INSCRIPTO(1),
    MONOTRIBUTO(2),
    EXENTO(3),
    NO_RESPONSABLE(4),
    CONSUMIDOR_FINAL(5);

    companion object {
        fun fromValue(value: Int): TributeCondition {
            return values().find { it.value == value } ?: CONSUMIDOR_FINAL
        }
    }

    override fun toString(): String {
        return when (this) {
            RESPONSABLE_INSCRIPTO -> "Responsable Inscripto"
            MONOTRIBUTO -> "Monotributista"
            EXENTO -> "Exento"
            NO_RESPONSABLE -> "No Responsable"
            CONSUMIDOR_FINAL -> "Consumidor Final"
        }
    }
}