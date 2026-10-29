package com.angel.monevia.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transacciones")
data class TransaccionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tipo: String,
    val monto: String,
    val categoria: String,
    val descripcion: String,
    val fecha: String
)