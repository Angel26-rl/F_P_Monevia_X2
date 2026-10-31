package com.angel.monevia

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angel.monevia.core.TipoTransaccion
import com.angel.monevia.data.local.TransaccionEntity
import com.angel.monevia.data.repository.TransaccionRepository
import com.angel.monevia.ui.theme.DMSerifDisplay
import com.angel.monevia.ui.theme.Inter
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Locale
import androidx.compose.foundation.layout.navigationBarsPadding
import java.time.YearMonth
import java.time.format.DateTimeFormatter

// ----------------------------------------------------
// PALETA DE COLORES
// ----------------------------------------------------

private val FondoFinanzas = Color(0xFF101412)
private val SuperficieFinanzas = Color(0xFF17201B)
private val SuperficieElevada = Color(0xFF202B24)
private val VerdeBosque = Color(0xFF173D2D)
private val VerdeFinanzas = Color(0xFF35D39A)
private val TextoFinanzas = Color(0xFFF1F5F2)
private val GrisFinanzas = Color(0xFFADB9B1)
private val RojoFinanzas = Color(0xFFFF6B72)

private val ColoresGrafico = listOf(
    Color(0xFF35D39A),
    Color(0xFF72B7A0),
    Color(0xFF86A8C2),
    Color(0xFFE5B66B),
    Color(0xFFC18BCE),
    Color(0xFFE47F85)
)

// ----------------------------------------------------
// FORMATO MONETARIO
// ----------------------------------------------------

private fun formatoMoneda(valor: BigDecimal): String {
    val simbolos = DecimalFormatSymbols(Locale.US)
    val formato = DecimalFormat("#,##0.00", simbolos)

    return "Q ${formato.format(valor)}"
}

private fun formatoMoneda(valor: String): String {
    val cantidad = valor.toBigDecimalOrNull() ?: BigDecimal.ZERO
    return formatoMoneda(cantidad)
}

// ----------------------------------------------------
// PANTALLA PRINCIPAL
// ----------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFinanzas(
    nombre: String,
    repositorio: TransaccionRepository,
    onEditarPerfil: () -> Unit
) {
    val movimientos by repositorio.obtenerTodas()
        .collectAsState(initial = emptyList())

    var mesSeleccionado by remember {
        mutableStateOf(YearMonth.now())
    }

    val movimientosDelMes = movimientos.filter {
        it.fecha.toString().take(7) == mesSeleccionado.toString()
    }

    val ingresos = movimientosDelMes
        .filter { it.tipo == TipoTransaccion.INGRESO.name }
        .fold(BigDecimal.ZERO) { total, item ->
            total.add(BigDecimal(item.monto))
        }

    val gastos = movimientosDelMes
        .filter { it.tipo == TipoTransaccion.GASTO.name }
        .fold(BigDecimal.ZERO) { total, item ->
            total.add(BigDecimal(item.monto))
        }

    val balance = ingresos.subtract(gastos)

    val scope = rememberCoroutineScope()

    var tipo by remember {
        mutableStateOf(TipoTransaccion.GASTO)
    }

    var monto by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Alimentación") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf(LocalDate.now().toString()) }

    var categoriaExpandida by remember { mutableStateOf(false) }
    var mostrarCalendario by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }

    val categorias = if (tipo == TipoTransaccion.INGRESO) {
        listOf("Salario", "Ventas", "Regalo", "Otros")
    } else {
        listOf(
            "Alimentación",
            "Transporte",
            "Estudios",
            "Salud",
            "Entretenimiento",
            "Otros"
        )
    }

    val gastosPorCategoria = movimientosDelMes
        .filter { it.tipo == TipoTransaccion.GASTO.name }
        .groupBy { it.categoria }
        .mapValues { (_, lista) ->
            lista.fold(BigDecimal.ZERO) { total, item ->
                total.add(BigDecimal(item.monto))
            }
        }
        .filterValues { it > BigDecimal.ZERO }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoFinanzas)
            .navigationBarsPadding(),
        contentPadding = PaddingValues(
            horizontal = 22.dp,
            vertical = 26.dp
        ),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {

        // ENCABEZADO
        item {
            Column {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Monevia",
                            color = VerdeFinanzas,
                            fontFamily = DMSerifDisplay,
                            fontSize = 29.sp
                        )

                        Text(
                            text = "FINANZAS PERSONALES",
                            color = GrisFinanzas,
                            fontFamily = Inter,
                            fontSize = 9.sp,
                            letterSpacing = 2.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(VerdeBosque, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = nombre.firstOrNull()?.uppercase() ?: "M",
                            color = VerdeFinanzas,
                            fontFamily = DMSerifDisplay,
                            fontSize = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(27.dp))

                Text(
                    text = "¡Hola, $nombre!",
                    color = TextoFinanzas,
                    fontFamily = DMSerifDisplay,
                    fontSize = 30.sp,
                    lineHeight = 38.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Este es el estado de tus finanzas.",
                    color = GrisFinanzas,
                    fontFamily = Inter,
                    fontSize = 14.sp
                )
            }
        }

        // SELECTOR DE MES
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SuperficieFinanzas, RoundedCornerShape(16.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { mesSeleccionado = mesSeleccionado.minusMonths(1) }
                ) {
                    Text("‹", color = VerdeFinanzas, fontSize = 30.sp)
                }

                Text(
                    text = mesSeleccionado.format(
                        DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "GT"))
                    ).replaceFirstChar { it.uppercase(Locale("es", "GT")) },
                    color = TextoFinanzas,
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )

                IconButton(
                    onClick = {
                        if (mesSeleccionado < YearMonth.now()) {
                            mesSeleccionado = mesSeleccionado.plusMonths(1)
                        }
                    },
                    enabled = mesSeleccionado < YearMonth.now()
                ) {
                    Text(
                        "›",
                        color = if (mesSeleccionado < YearMonth.now()) {
                            VerdeFinanzas
                        } else {
                            GrisFinanzas.copy(alpha = 0.35f)
                        },
                        fontSize = 30.sp
                    )
                }
            }
        }

        // BALANCE DISPONIBLE
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = CardDefaults.cardColors(
                    containerColor = VerdeBosque
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(23.dp)
                ) {
                    Text(
                        text = "BALANCE DISPONIBLE",
                        color = GrisFinanzas,
                        fontFamily = Inter,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.8.sp
                    )

                    Spacer(modifier = Modifier.height(13.dp))

                    Text(
                        text = formatoMoneda(balance),
                        color = if (balance < BigDecimal.ZERO) {
                            RojoFinanzas
                        } else {
                            TextoFinanzas
                        },
                        fontFamily = DMSerifDisplay,
                        fontSize = 34.sp,
                        lineHeight = 42.sp
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                TextoFinanzas.copy(alpha = 0.15f)
                            )
                    )

                    Spacer(modifier = Modifier.height(17.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    Modifier
                                        .size(9.dp)
                                        .background(VerdeFinanzas, CircleShape)
                                )
                                Spacer(modifier = Modifier.size(7.dp))
                                Text(
                                    text = "Ingresos",
                                    color = GrisFinanzas,
                                    fontFamily = Inter,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(7.dp))

                            Text(
                                text = formatoMoneda(ingresos),
                                color = VerdeFinanzas,
                                fontFamily = Inter,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    Modifier
                                        .size(9.dp)
                                        .background(RojoFinanzas, CircleShape)
                                )
                                Spacer(modifier = Modifier.size(7.dp))
                                Text(
                                    text = "Gastos",
                                    color = GrisFinanzas,
                                    fontFamily = Inter,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(7.dp))

                            Text(
                                text = formatoMoneda(gastos),
                                color = RojoFinanzas,
                                fontFamily = Inter,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // TARJETAS RESUMEN
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaResumen(
                    titulo = "Ingresos",
                    cantidad = formatoMoneda(ingresos),
                    indicador = "↑",
                    color = VerdeFinanzas,
                    modifier = Modifier.weight(1f)
                )

                TarjetaResumen(
                    titulo = "Gastos",
                    cantidad = formatoMoneda(gastos),
                    indicador = "↓",
                    color = RojoFinanzas,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // GRÁFICO DE GASTOS
        item {
            Column {
                Text(
                    text = "Distribución de gastos",
                    color = TextoFinanzas,
                    fontFamily = DMSerifDisplay,
                    fontSize = 25.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Así se distribuye tu dinero.",
                    color = GrisFinanzas,
                    fontFamily = Inter,
                    fontSize = 13.sp
                )
            }
        }

        item {
            TarjetaGrafico(
                gastosPorCategoria = gastosPorCategoria,
                totalGastos = gastos
            )
        }

        // FORMULARIO
        item {
            Column {
                Text(
                    text = "Nuevo movimiento",
                    color = TextoFinanzas,
                    fontFamily = DMSerifDisplay,
                    fontSize = 25.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Registra tus ingresos y gastos.",
                    color = GrisFinanzas,
                    fontFamily = Inter,
                    fontSize = 13.sp
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = SuperficieFinanzas
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(17.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    // TIPO
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                tipo = TipoTransaccion.INGRESO
                                categoria = "Salario"
                                mensaje = ""
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(13.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (
                                    tipo == TipoTransaccion.INGRESO
                                ) VerdeFinanzas else SuperficieElevada,
                                contentColor = if (
                                    tipo == TipoTransaccion.INGRESO
                                ) FondoFinanzas else TextoFinanzas
                            )
                        ) {
                            Text(
                                "↑ Ingreso",
                                fontFamily = Inter,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Button(
                            onClick = {
                                tipo = TipoTransaccion.GASTO
                                categoria = "Alimentación"
                                mensaje = ""
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(13.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (
                                    tipo == TipoTransaccion.GASTO
                                ) RojoFinanzas else SuperficieElevada,
                                contentColor = if (
                                    tipo == TipoTransaccion.GASTO
                                ) FondoFinanzas else TextoFinanzas
                            )
                        ) {
                            Text(
                                "↓ Gasto",
                                fontFamily = Inter,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // MONTO
                    OutlinedTextField(
                        value = monto,
                        onValueChange = {
                            monto = it
                        },
                        label = { Text("Monto en quetzales (Q)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = coloresCampos()
                    )

                    // CATEGORIA
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
                                .menuAnchor(),
                            shape = RoundedCornerShape(14.dp),
                            colors = coloresCampos()
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

                    // DESCRIPCION
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = {
                            descripcion = it
                        },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = coloresCampos()
                    )

                    // FECHA CON CALENDARIO
                    OutlinedTextField(
                        value = fecha,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Fecha del movimiento") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    mostrarCalendario = true
                                }
                            ) {
                                Text(
                                    "▦",
                                    color = VerdeFinanzas,
                                    fontSize = 22.sp
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                awaitEachGesture {
                                    awaitFirstDown(
                                        pass = PointerEventPass.Initial
                                    )

                                    val upEvent = waitForUpOrCancellation(
                                        pass = PointerEventPass.Initial
                                    )

                                    if (upEvent != null) {
                                        mostrarCalendario = true
                                    }
                                }
                            },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = coloresCampos()
                    )

                    // GUARDAR
                    Button(
                        onClick = {
                            val cantidad = monto.trim().toBigDecimalOrNull()

                            val fechaValida = runCatching {
                                LocalDate.parse(fecha.trim())
                            }.getOrNull()

                            mensaje = when {
                                cantidad == null ||
                                        cantidad <= BigDecimal.ZERO ->
                                    "Ingresa un monto mayor que cero."

                                fechaValida == null ->
                                    "Selecciona una fecha válida."

                                descripcion.isBlank() ->
                                    "Escribe una descripción."

                                else -> ""
                            }

                            if (
                                mensaje.isEmpty() &&
                                cantidad != null &&
                                fechaValida != null
                            ) {
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
                            .height(53.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdeFinanzas,
                            contentColor = FondoFinanzas
                        )
                    ) {
                        Text(
                            "Guardar movimiento",
                            fontFamily = Inter,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    if (mensaje.isNotBlank()) {
                        Text(
                            text = mensaje,
                            color = if (
                                mensaje.startsWith("No se pudo") ||
                                mensaje.startsWith("Ingresa") ||
                                mensaje.startsWith("Selecciona") ||
                                mensaje.startsWith("Escribe")
                            ) RojoFinanzas else VerdeFinanzas,
                            fontFamily = Inter,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // HISTORIAL
        item {
            Column {
                Text(
                    text = "Tus movimientos",
                    color = TextoFinanzas,
                    fontFamily = DMSerifDisplay,
                    fontSize = 25.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "${movimientosDelMes.size} movimientos registrados",
                    color = GrisFinanzas,
                    fontFamily = Inter,
                    fontSize = 13.sp
                )
            }
        }

        if (movimientosDelMes.isEmpty()) {
            item {
                TarjetaVacia()
            }
        } else {
            items(
                items = movimientosDelMes,
                key = { it.id }
            ) { movimiento ->
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

        // EDITAR PERFIL
        item {
            TextButton(
                onClick = onEditarPerfil,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Editar mis datos personales",
                    color = GrisFinanzas,
                    fontFamily = Inter,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
        }
    }

    // DIALOGO DE CALENDARIO
    if (mostrarCalendario) {
        val fechaInicialCalendario = runCatching {
            LocalDate.parse(fecha)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli()
        }.getOrNull()

        val estadoCalendario = rememberDatePickerState(
            initialSelectedDateMillis = fechaInicialCalendario
        )

        DatePickerDialog(
            onDismissRequest = {
                mostrarCalendario = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        estadoCalendario.selectedDateMillis?.let {
                            fecha = Instant
                                .ofEpochMilli(it)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                                .toString()
                        }

                        mostrarCalendario = false
                    },
                    enabled = estadoCalendario.selectedDateMillis != null
                ) {
                    Text("Aceptar", color = VerdeFinanzas)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarCalendario = false
                    }
                ) {
                    Text("Cancelar", color = GrisFinanzas)
                }
            }
        ) {
            DatePicker(
                state = estadoCalendario,
                title = {
                    Text("Fecha del movimiento")
                }
            )
        }
    }
}

// ----------------------------------------------------
// TARJETA RESUMEN
// ----------------------------------------------------

@Composable
private fun TarjetaResumen(
    titulo: String,
    cantidad: String,
    indicador: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = SuperficieFinanzas
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    indicador,
                    color = color,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.size(7.dp))

                Text(
                    titulo,
                    color = GrisFinanzas,
                    fontFamily = Inter,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                cantidad,
                color = color,
                fontFamily = Inter,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

// ----------------------------------------------------
// GRAFICO CIRCULAR
// ----------------------------------------------------

@Composable
private fun TarjetaGrafico(
    gastosPorCategoria: Map<String, BigDecimal>,
    totalGastos: BigDecimal
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = SuperficieFinanzas
        )
    ) {
        if (gastosPorCategoria.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "◌",
                    color = VerdeFinanzas,
                    fontSize = 38.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "Aún no hay gastos para analizar",
                    color = TextoFinanzas,
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    "Tus categorías aparecerán aquí.",
                    color = GrisFinanzas,
                    fontFamily = Inter,
                    fontSize = 12.sp
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(19.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(205.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier.size(190.dp)
                    ) {
                        var anguloInicial = -90f

                        val total = totalGastos.toFloat()

                        if (total > 0f) {
                            gastosPorCategoria.entries.forEachIndexed {
                                    index, entry ->

                                val porcentaje =
                                    entry.value.toFloat() / total

                                val barrido = porcentaje * 360f

                                drawArc(
                                    color = ColoresGrafico[
                                        index % ColoresGrafico.size
                                    ],
                                    startAngle = anguloInicial,
                                    sweepAngle = barrido,
                                    useCenter = false,
                                    style = Stroke(
                                        width = 25.dp.toPx(),
                                        cap = StrokeCap.Butt
                                    ),
                                    size = Size(
                                        size.width,
                                        size.height
                                    )
                                )

                                anguloInicial += barrido
                            }
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "GASTOS",
                            color = GrisFinanzas,
                            fontFamily = Inter,
                            fontSize = 10.sp,
                            letterSpacing = 1.5.sp
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            formatoMoneda(totalGastos),
                            color = TextoFinanzas,
                            fontFamily = DMSerifDisplay,
                            fontSize = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                gastosPorCategoria.entries.forEachIndexed {
                        index, entry ->

                    val porcentaje = if (
                        totalGastos > BigDecimal.ZERO
                    ) {
                        entry.value
                            .multiply(BigDecimal("100"))
                            .divide(
                                totalGastos,
                                1,
                                java.math.RoundingMode.HALF_UP
                            )
                    } else {
                        BigDecimal.ZERO
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(
                                    ColoresGrafico[
                                        index % ColoresGrafico.size
                                    ],
                                    CircleShape
                                )
                        )

                        Spacer(modifier = Modifier.size(10.dp))

                        Text(
                            entry.key,
                            modifier = Modifier.weight(1f),
                            color = TextoFinanzas,
                            fontFamily = Inter,
                            fontSize = 12.sp
                        )

                        Text(
                            "${porcentaje.toPlainString()}%",
                            color = GrisFinanzas,
                            fontFamily = Inter,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.size(10.dp))

                        Text(
                            formatoMoneda(entry.value),
                            color = TextoFinanzas,
                            fontFamily = Inter,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// ESTADO VACIO DEL HISTORIAL
// ----------------------------------------------------

@Composable
private fun TarjetaVacia() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = SuperficieFinanzas
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "↗",
                color = VerdeFinanzas,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "Todavía no hay movimientos",
                color = TextoFinanzas,
                fontFamily = Inter,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                "Cuando registres uno, aparecerá aquí.",
                color = GrisFinanzas,
                fontFamily = Inter,
                fontSize = 12.sp
            )
        }
    }
}

// ----------------------------------------------------
// COLORES DE LOS CAMPOS
// ----------------------------------------------------

@Composable
private fun coloresCampos() =
    androidx.compose.material3.OutlinedTextFieldDefaults.colors(
        focusedTextColor = TextoFinanzas,
        unfocusedTextColor = TextoFinanzas,
        focusedBorderColor = VerdeFinanzas,
        unfocusedBorderColor = Color(0xFF34443A),
        focusedLabelColor = VerdeFinanzas,
        unfocusedLabelColor = GrisFinanzas,
        focusedPlaceholderColor = GrisFinanzas,
        unfocusedPlaceholderColor = GrisFinanzas,
        focusedContainerColor = SuperficieElevada,
        unfocusedContainerColor = SuperficieElevada,
        cursorColor = VerdeFinanzas
    )

// ----------------------------------------------------
// TARJETA DE MOVIMIENTO
// ----------------------------------------------------

@Composable
private fun TarjetaMovimiento(
    movimiento: TransaccionEntity,
    onEliminar: () -> Unit
) {
    val esIngreso =
        movimiento.tipo == TipoTransaccion.INGRESO.name

    val colorMovimiento = if (esIngreso) {
        VerdeFinanzas
    } else {
        RojoFinanzas
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = SuperficieFinanzas
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            colorMovimiento.copy(alpha = 0.13f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (esIngreso) "↑" else "↓",
                        color = colorMovimiento,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        movimiento.descripcion,
                        color = TextoFinanzas,
                        fontFamily = Inter,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        movimiento.categoria,
                        color = GrisFinanzas,
                        fontFamily = Inter,
                        fontSize = 11.sp
                    )

                    Text(
                        movimiento.fecha.toString(),
                        color = GrisFinanzas,
                        fontFamily = Inter,
                        fontSize = 10.sp
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${if (esIngreso) "+" else "-"} ${formatoMoneda(BigDecimal(movimiento.monto))}",
                    color = colorMovimiento,
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )

                TextButton(
                    onClick = onEliminar
                ) {
                    Text(
                        "Eliminar",
                        color = GrisFinanzas,
                        fontFamily = Inter,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
