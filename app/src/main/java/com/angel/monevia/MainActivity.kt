package com.angel.monevia

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angel.monevia.ui.theme.MoneviaTheme
import java.time.LocalDate
import com.angel.monevia.data.local.MoneviaDatabase
import com.angel.monevia.data.repository.TransaccionRepository

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

@Composable
fun PantallaBienvenida(
    onComenzar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = VerdeBosque,
                    shape = RoundedCornerShape(28.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "M",
                color = Esmeralda,
                fontSize = 58.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Monevia",
            color = BlancoHumo,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Tu dinero. Tu camino. Tu control.",
            color = TextoSecundario,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onComenzar,
            modifier = Modifier
                .width(280.dp)
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Esmeralda,
                contentColor = Fondo
            )
        ) {
            Text(
                text = "Comenzar",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Organiza tu dinero con claridad.",
            color = TextoSecundario,
            fontSize = 13.sp
        )
    }
}

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
    var mensaje by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (nombreInicial.isBlank()) "Tus datos" else "Editar mis datos",
            color = BlancoHumo,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Personalicemos tu experiencia",
            color = TextoSecundario,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        CampoRegistro(
            valor = nombre,
            alCambiar = { nombre = it },
            etiqueta = "Nombre completo"
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoRegistro(
            valor = correo,
            alCambiar = { correo = it },
            etiqueta = "Correo electrónico"
        )

        Spacer(modifier = Modifier.height(12.dp))

        CampoRegistro(
            valor = fechaNacimiento,
            alCambiar = { fechaNacimiento = it },
            etiqueta = "Fecha de nacimiento (AAAA-MM-DD)"
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = Color(0xFFFF6B72),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))
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
                        "Usa una fecha válida: AAAA-MM-DD."

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
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Esmeralda,
                contentColor = Fondo
            )
        ) {
            Text(
                text = "Guardar y continuar",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onVolver) {
            Text(
                text = "Volver",
                color = Esmeralda
            )
        }
    }
}

@Composable
private fun CampoRegistro(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = {
            Text(etiqueta)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = androidx.compose.material3
            .OutlinedTextFieldDefaults.colors(
                focusedTextColor = BlancoHumo,
                unfocusedTextColor = BlancoHumo,
                focusedBorderColor = Esmeralda,
                unfocusedBorderColor = VerdeBosque,
                focusedLabelColor = Esmeralda,
                unfocusedLabelColor = TextoSecundario,
                cursorColor = Esmeralda
            )
    )
}

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

