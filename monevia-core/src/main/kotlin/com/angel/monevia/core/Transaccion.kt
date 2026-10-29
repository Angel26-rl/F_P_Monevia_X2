package com.angel.monevia.core

import java.math.BigDecimal
import java.time.LocalDate

enum class TipoTransaccion {
    INGRESO,
    GASTO
}

data class Transaccion(
    val id: Long = 0,
    val tipo: TipoTransaccion,
    val monto: BigDecimal,
    val categoria: String,
    val descripcion: String,
    val fecha: LocalDate
)
