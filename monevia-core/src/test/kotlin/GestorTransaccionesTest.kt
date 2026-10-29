package com.angel.monevia.core

import java.math.BigDecimal
import java.time.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GestorTransaccionesTest {

    @Test
    fun `calcular balance con ingresos y gastos`() {
        val gestor = GestorTransacciones()

        gestor.registrar(
            Transaccion(
                tipo = TipoTransaccion.INGRESO,
                monto = BigDecimal("1000.00"),
                categoria = "Salario",
                descripcion = "Pago mensual",
                fecha = LocalDate.of(2026, 10, 23)
            )
        )

        gestor.registrar(
            Transaccion(
                tipo = TipoTransaccion.GASTO,
                monto = BigDecimal("1200.00"),
                categoria = "Alimentación",
                descripcion = "Compras",
                fecha = LocalDate.of(2026, 10, 23)
            )
        )

        assertEquals(
            BigDecimal("1000.00"),
            gestor.calcularIngresos()
        )

        assertEquals(
            BigDecimal("1200.00"),
            gestor.calcularGastos()
        )

        assertEquals(
            BigDecimal("-200.00"),
            gestor.calcularBalance()
        )
    }
}
