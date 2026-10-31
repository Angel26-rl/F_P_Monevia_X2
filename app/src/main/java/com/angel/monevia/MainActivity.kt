
package com.angel.monevia

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angel.monevia.data.local.MoneviaDatabase
import com.angel.monevia.data.repository.TransaccionRepository
import com.angel.monevia.ui.theme.DMSerifDisplay
import com.angel.monevia.ui.theme.Inter
import com.angel.monevia.ui.theme.MoneviaTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

private val Fondo = Color(0xFF101412)
private val VerdeBosque = Color(0xFF173D2D)
private val Esmeralda = Color(0xFF35D39A)
private val BlancoHumo = Color(0xFFF1F5F2)
private val TextoSecundario = Color(0xFFADB9B1)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferencias = getSharedPreferences(
            "monevia_perfil",
            MODE_PRIVATE
        )

        val baseDatos = MoneviaDatabase.obtenerInstancia(this)
        val repositorio = TransaccionRepository(baseDatos.transaccionDao())

        setContent {
            MoneviaTheme {
                var pantallaActual by remember {
                    mutableStateOf(
                        if (preferencias.getBoolean("perfil_completo", false)) {
                            "inicio"
                        } else {
                            "bienvenida"
                        }
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Fondo
                ) {
                    when (pantallaActual) {

                        "bienvenida" -> {
                            PantallaBienvenida(
                                onComenzar = {
                                    pantallaActual = "registro"
                                }
                            )
                        }

                        "registro" -> {
                            PantallaRegistro(
                                nombreInicial = preferencias.getString(
                                    "nombre", ""
                                ) ?: "",

                                correoInicial = preferencias.getString(
                                    "correo", ""
                                ) ?: "",

                                fechaInicial = preferencias.getString(
                                    "fecha_nacimiento", ""
                                ) ?: "",

                                onGuardar = { nombre, correo, fecha ->
                                    preferencias.edit()
                                        .putString("nombre", nombre)
                                        .putString("correo", correo)
                                        .putString("fecha_nacimiento", fecha)
                                        .putBoolean("perfil_completo", true)
                                        .apply()

                                    pantallaActual = "inicio"
                                },

                                onVolver = {
                                    pantallaActual =
                                        if (preferencias.getBoolean(
                                                "perfil_completo", false
                                            )
                                        ) {
                                            "inicio"
                                        } else {
                                            "bienvenida"
                                        }
                                }
                            )
                        }

                        else -> {
                            PantallaFinanzas(
                                nombre = preferencias.getString(
                                    "nombre", ""
                                ) ?: "",

                                repositorio = repositorio,

                                onEditarPerfil = {
                                    pantallaActual = "registro"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


// ----------------------------------------------------
// PANTALLA DE BIENVENIDA
// ----------------------------------------------------

@Composable
fun PantallaBienvenida(
    onComenzar: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF14231B),
                        Fondo,
                        Fondo
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        // Elemento decorativo de fondo
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 65.dp, y = 100.dp)
                .size(190.dp)
                .clip(CircleShape)
                .background(
                    Esmeralda.copy(alpha = 0.045f)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Logotipo
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(VerdeBosque),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "M",
                    color = Esmeralda,
                    fontFamily = DMSerifDisplay,
                    fontSize = 76.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Monevia",
                color = BlancoHumo,
                fontFamily = DMSerifDisplay,
                fontSize = 48.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "FINANZAS PERSONALES",
                color = Esmeralda,
                fontFamily = Inter,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Tu dinero. Tu camino.\nTu control.",
                color = TextoSecundario,
                fontFamily = Inter,
                fontSize = 16.sp,
                lineHeight = 26.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(58.dp))

            Button(
                onClick = onComenzar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Esmeralda,
                    contentColor = Fondo
                )
            ) {
                Text(
                    text = "Comenzar",
                    fontFamily = Inter,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Organiza tu dinero con claridad.",
                color = TextoSecundario,
                fontFamily = Inter,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}


// ----------------------------------------------------
// PANTALLA DE REGISTRO
// ----------------------------------------------------

@Composable
fun PantallaRegistro(
    nombreInicial: String,
    correoInicial: String,
    fechaInicial: String,
    onGuardar: (String, String, String) -> Unit,
    onVolver: () -> Unit
) {
    var nombre by remember(nombreInicial) {
        mutableStateOf(nombreInicial)
    }

    var correo by remember(correoInicial) {
        mutableStateOf(correoInicial)
    }

    var fechaNacimiento by remember(fechaInicial) {
        mutableStateOf(fechaInicial)
    }

    var mostrarCalendario by remember {
        mutableStateOf(false)
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 26.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (nombreInicial.isBlank()) "Tus datos" else "Tu perfil",
            color = BlancoHumo,
            fontFamily = DMSerifDisplay,
            fontSize = 38.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Personalicemos tu experiencia",
            color = TextoSecundario,
            fontFamily = Inter,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "NOMBRE COMPLETO",
            color = Esmeralda,
            fontFamily = Inter,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        CampoRegistro(
            valor = nombre,
            alCambiar = { nombre = it },
            etiqueta = "Tu nombre"
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "CORREO ELECTRÓNICO",
            color = Esmeralda,
            fontFamily = Inter,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        CampoRegistro(
            valor = correo,
            alCambiar = { correo = it },
            etiqueta = "correo@ejemplo.com",
            tipo = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "FECHA DE NACIMIENTO",
            color = Esmeralda,
            fontFamily = Inter,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Campo de fecha con calendario funcional
        OutlinedTextField(
            value = fechaNacimiento,
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text("Selecciona tu fecha")
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        mostrarCalendario = true
                    }
                ) {
                    Text(
                        text = "▦",
                        color = Esmeralda,
                        fontSize = 23.sp
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
            shape = RoundedCornerShape(16.dp),
            colors = androidx.compose.material3
                .OutlinedTextFieldDefaults.colors(
                    focusedTextColor = BlancoHumo,
                    unfocusedTextColor = BlancoHumo,
                    focusedBorderColor = Esmeralda,
                    unfocusedBorderColor = VerdeBosque,
                    focusedPlaceholderColor = TextoSecundario,
                    unfocusedPlaceholderColor = TextoSecundario,
                    focusedContainerColor = Color(0xFF17201B),
                    unfocusedContainerColor = Color(0xFF17201B),
                    cursorColor = Esmeralda
                )
        )

        Spacer(modifier = Modifier.height(30.dp))

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = Color(0xFFFF6B72),
                fontFamily = Inter,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(15.dp))
        }

        Button(
            onClick = {
                val fecha = runCatching {
                    LocalDate.parse(fechaNacimiento.trim())
                }.getOrNull()

                mensaje = when {
                    nombre.isBlank() ->
                        "Escribe tu nombre completo."

                    !Patterns.EMAIL_ADDRESS
                        .matcher(correo.trim())
                        .matches() ->
                        "Introduce un correo electrónico válido."

                    fecha == null ->
                        "Selecciona tu fecha de nacimiento."

                    fecha.isAfter(LocalDate.now()) ->
                        "La fecha no puede ser futura."

                    else -> ""
                }

                if (mensaje.isEmpty()) {
                    onGuardar(
                        nombre.trim(),
                        correo.trim(),
                        fechaNacimiento.trim()
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(17.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Esmeralda,
                contentColor = Fondo
            )
        ) {
            Text(
                text = "Guardar y continuar",
                fontFamily = Inter,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Volver",
                color = TextoSecundario,
                fontFamily = Inter,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    // Calendario de fecha de nacimiento
    if (mostrarCalendario) {

        val fechaInicialCalendario = runCatching {
            LocalDate.parse(fechaNacimiento)
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
                            fechaNacimiento = Instant
                                .ofEpochMilli(it)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                                .toString()
                        }

                        mostrarCalendario = false
                    },
                    enabled = estadoCalendario.selectedDateMillis != null
                ) {
                    Text(
                        "Aceptar",
                        color = Esmeralda
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarCalendario = false
                    }
                ) {
                    Text(
                        "Cancelar",
                        color = TextoSecundario
                    )
                }
            }
        ) {
            DatePicker(
                state = estadoCalendario,
                title = {
                    Text("Fecha de nacimiento")
                }
            )
        }
    }
}


// ----------------------------------------------------
// CAMPOS DEL FORMULARIO
// ----------------------------------------------------

@Composable
private fun CampoRegistro(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    tipo: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        placeholder = {
            Text(etiqueta)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = tipo
        ),
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3
            .OutlinedTextFieldDefaults.colors(
                focusedTextColor = BlancoHumo,
                unfocusedTextColor = BlancoHumo,
                focusedBorderColor = Esmeralda,
                unfocusedBorderColor = VerdeBosque,
                focusedPlaceholderColor = TextoSecundario,
                unfocusedPlaceholderColor = TextoSecundario,
                focusedContainerColor = Color(0xFF17201B),
                unfocusedContainerColor = Color(0xFF17201B),
                cursorColor = Esmeralda
            )
    )
}


// ----------------------------------------------------
// PANTALLA DE INICIO ANTERIOR
// Se conserva para no eliminar código existente.
// ----------------------------------------------------

@Composable
fun PantallaInicio(
    nombre: String,
    correo: String,
    fechaNacimiento: String,
    onEditar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Monevia",
            color = Esmeralda,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "¡Hola, $nombre!",
            color = BlancoHumo,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tu espacio financiero está listo.",
            color = TextoSecundario,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Datos personales",
            color = BlancoHumo,
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Correo: $correo",
            color = TextoSecundario,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Nacimiento: $fechaNacimiento",
            color = TextoSecundario,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onEditar,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Esmeralda,
                contentColor = Fondo
            )
        ) {
            Text(
                text = "Editar mis datos",
                fontWeight = FontWeight.Bold
            )
        }
    }
}
