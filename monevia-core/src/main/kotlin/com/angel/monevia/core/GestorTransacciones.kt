package com.angel.monevia.core

import java.math.BigDecimal

class GestorTransacciones {

    private val transacciones = mutableListOf<Transaccion>()

    fun registrar(transaccion: Transaccion) {
        require(transaccion.monto > BigDecimal.ZERO) {
            "El monto debe ser mayor que cero."
        }

        transacciones.add(transaccion)
    }

    fun obtenerTransacciones(): List<Transaccion> {
        return transacciones.toList()
    }

    fun calcularIngresos(): BigDecimal {
        return transacciones
            .filter { it.tipo == TipoTransaccion.INGRESO }
            .fold(BigDecimal.ZERO) { total, transaccion ->
                total.add(transaccion.monto)
            }
    }

    fun calcularGastos(): BigDecimal {
        return transacciones
            .filter { it.tipo == TipoTransaccion.GASTO }
            .fold(BigDecimal.ZERO) { total, transaccion ->
                total.add(transaccion.monto)
            }
    }

    fun calcularBalance(): BigDecimal {
        return calcularIngresos().subtract(calcularGastos())
    }
}

