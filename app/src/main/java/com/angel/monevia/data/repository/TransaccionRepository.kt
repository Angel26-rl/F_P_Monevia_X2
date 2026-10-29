package com.angel.monevia.data.repository

import com.angel.monevia.core.GestorTransacciones
import com.angel.monevia.core.TipoTransaccion
import com.angel.monevia.core.Transaccion
import com.angel.monevia.data.local.TransaccionDao
import com.angel.monevia.data.local.TransaccionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.time.LocalDate

class TransaccionRepository(
    private val dao: TransaccionDao
) {
    fun obtenerTodas(): Flow<List<TransaccionEntity>> =
        dao.obtenerTodas()

    suspend fun registrar(
        tipo: TipoTransaccion,
        monto: BigDecimal,
        categoria: String,
        descripcion: String,
        fecha: LocalDate
    ) {
        val transaccion = Transaccion(
            tipo = tipo,
            monto = monto,
            categoria = categoria,
            descripcion = descripcion,
            fecha = fecha
        )

        // Validación del movimiento con la biblioteca de negocio.
        GestorTransacciones().registrar(transaccion)

        dao.insertar(
            TransaccionEntity(
                tipo = tipo.name,
                monto = monto.toPlainString(),
                categoria = categoria,
                descripcion = descripcion,
                fecha = fecha.toString()
            )
        )
    }

    suspend fun eliminar(transaccion: TransaccionEntity) {
        dao.eliminar(transaccion)
    }

    fun obtenerBalance(movimientos: List<TransaccionEntity>): BigDecimal {
        val ingresos = movimientos
            .filter { it.tipo == TipoTransaccion.INGRESO.name }
            .fold(BigDecimal.ZERO) { total, item ->
                total.add(BigDecimal(item.monto))
            }

        val gastos = movimientos
            .filter { it.tipo == TipoTransaccion.GASTO.name }
            .fold(BigDecimal.ZERO) { total, item ->
                total.add(BigDecimal(item.monto))
            }

        return ingresos.subtract(gastos)
    }
}