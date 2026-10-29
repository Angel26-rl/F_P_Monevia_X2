package com.angel.monevia.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaccionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(transaccion: TransaccionEntity): Long

    @Query("SELECT * FROM transacciones ORDER BY fecha DESC, id DESC")
    fun obtenerTodas(): Flow<List<TransaccionEntity>>

    @Delete
    suspend fun eliminar(transaccion: TransaccionEntity)

    @Query("SELECT * FROM transacciones WHERE fecha = :fecha ORDER BY id DESC")
    fun buscarPorFecha(fecha: String): Flow<List<TransaccionEntity>>
}