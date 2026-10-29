
package com.angel.monevia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angel.monevia.core.TipoTransaccion
import com.angel.monevia.data.local.TransaccionEntity
import com.angel.monevia.data.repository.TransaccionRepository
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

private val FondoFinanzas = Color(0xFF101412)
private val VerdeFinanzas = Color(0xFF35D39A)
private val TextoFinanzas = Color(0xFFF1F5F2)
private val GrisFinanzas = Color(0xFFADB9B1)
private val RojoFinanzas = Color(0xFFFF6B72)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFinanzas(
    nombre: String,
    repositorio: TransaccionRepository,
    onEditarPerfil: () -> Unit
) {
    val movimientos by repositorio.obtenerTodas()
        .collectAsState(initial = emptyList())

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

    val balance = ingresos.subtract(gastos)
    val scope = rememberCoroutineScope()

    var tipo by remember { mutableStateOf(TipoTransaccion.GASTO) }
    var monto by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Otros") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf(LocalDate.now().toString()) }
    var categoriaExpandida by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }

    val categorias = if (tipo == TipoTransaccion.INGRESO) {
        listOf("Salario", "Ventas", "Regalo", "Otros")
    } else {
        listOf("Alimentación", "Transporte", "Estudios", "Salud", "Entretenimiento", "Otros")
    }

    Scaffold(
        containerColor = FondoFinanzas,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Monevia",
                        color = VerdeFinanzas,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    "¡Hola, $nombre!",
                    color = TextoFinanzas,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Tu resumen financiero",
                    color = GrisFinanzas
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF173D2D)
                    )
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text("Balance disponible", color = GrisFinanzas)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Q ${balance.toPlainString()}",
                            color = if (balance < BigDecimal.ZERO) {
                                RojoFinanzas
                            } else {
                                VerdeFinanzas
                            },
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(18.dp))
                        Row(Modifier.fillMaxWidth()) {
                            Column(Modifier.weight(1f)) {
                                Text("Ingresos", color = GrisFinanzas)
                                Text(
                                    "Q ${ingresos.toPlainString()}",
                                    color = VerdeFinanzas,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Column(Modifier.weight(1f)) {
                                Text("Gastos", color = GrisFinanzas)
                                Text(
                                    "Q ${gastos.toPlainString()}",
                                    color = RojoFinanzas,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    "Registrar movimiento",
                    color = TextoFinanzas,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            tipo = TipoTransaccion.INGRESO
                            categoria = "Salario"
                            mensaje = ""
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (tipo == TipoTransaccion.INGRESO) {
                                VerdeFinanzas
                            } else Color(0xFF26312B),
                            contentColor = if (tipo == TipoTransaccion.INGRESO) {
                                FondoFinanzas
                            } else TextoFinanzas
                        )
                    ) {
                        Text("Ingreso")
                    }

                    Button(
                        onClick = {
                            tipo = TipoTransaccion.GASTO
                            categoria = "Alimentación"
                            mensaje = ""
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (tipo == TipoTransaccion.GASTO) {
                                RojoFinanzas
                            } else Color(0xFF26312B),
                            contentColor = if (tipo == TipoTransaccion.GASTO) {
                                FondoFinanzas
                            } else TextoFinanzas
                        )
                    ) {
                        Text("Gasto")
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = { Text("Monto en quetzales (Q)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )
            }

            item {
                ExposedDropdownMenuBox(
                    expanded = categoriaExpandida,
                    onExpandedChange = {
                        categoriaExpandida = !categoriaExpandida
                    }
                ) {
                    OutlinedTextField(
                        value = categoria,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = categoriaExpandida
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = categoriaExpandida,
                        onDismissRequest = {
                            categoriaExpandida = false
                        }
                    ) {
                        categorias.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    categoria = opcion
                                    categoriaExpandida = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha (AAAA-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                Button(
                    onClick = {
                        val cantidad = monto.trim().toBigDecimalOrNull()
                        val fechaValida = runCatching {
                            LocalDate.parse(fecha.trim())
                        }.getOrNull()

                        mensaje = when {
                            cantidad == null || cantidad <= BigDecimal.ZERO ->
                                "Ingresa un monto mayor que cero."
                            fechaValida == null ->
                                "La fecha debe tener formato AAAA-MM-DD."
                            descripcion.isBlank() ->
                                "Escribe una descripción."
                            else -> ""
                        }

                        if (mensaje.isEmpty() && cantidad != null && fechaValida != null) {
                            scope.launch {
                                try {
                                    repositorio.registrar(
                                        tipo = tipo,
                                        monto = cantidad,
                                        categoria = categoria,
                                        descripcion = descripcion.trim(),
                                        fecha = fechaValida
                                    )
                                    monto = ""
                                    descripcion = ""
                                    fecha = LocalDate.now().toString()
                                    mensaje = "Movimiento guardado correctamente."
                                } catch (e: Exception) {
                                    mensaje = "No se pudo guardar el movimiento."
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeFinanzas,
                        contentColor = FondoFinanzas
                    )
                ) {
                    Text(
                        "Guardar movimiento",
                        fontWeight = FontWeight.Bold
                    )
                }

                if (mensaje.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        mensaje,
                        color = if (mensaje.startsWith("No se pudo")) {
                            RojoFinanzas
                        } else GrisFinanzas
                    )
                }
            }

            item {
                Text(
                    "Historial de movimientos",
                    color = TextoFinanzas,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (movimientos.isEmpty()) {
                item {
                    Text(
                        "Todavía no hay movimientos registrados.",
                        color = GrisFinanzas
                    )
                }
            } else {
                items(movimientos, key = { it.id }) { movimiento ->
                    TarjetaMovimiento(
                        movimiento = movimiento,
                        onEliminar = {
                            scope.launch {
                                repositorio.eliminar(movimiento)
                            }
                        }
                    )
                }
            }

            item {
                Button(
                    onClick = onEditarPerfil,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF26312B),
                        contentColor = TextoFinanzas
                    )
                ) {
                    Text("Editar mis datos personales")
                }
            }
        }
    }
}

@Composable
private fun TarjetaMovimiento(
    movimiento: TransaccionEntity,
    onEliminar: () -> Unit
) {
    val esIngreso = movimiento.tipo == TipoTransaccion.INGRESO.name

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B241F)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    movimiento.descripcion,
                    color = TextoFinanzas,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "${movimiento.categoria} · ${movimiento.fecha}",
                    color = GrisFinanzas,
                    fontSize = 12.sp
                )
            }

            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                Text(
                    "${if (esIngreso) "+" else "-"} Q ${movimiento.monto}",
                    color = if (esIngreso) VerdeFinanzas else RojoFinanzas,
                    fontWeight = FontWeight.Bold
                )
                androidx.compose.material3.TextButton(onClick = onEliminar) {
                    Text("Eliminar", color = GrisFinanzas)
                }
            }
        }
    }
}
