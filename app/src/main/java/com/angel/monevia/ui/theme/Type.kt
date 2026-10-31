package com.angel.monevia.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.angel.monevia.R

val Inter = FontFamily(
    Font(R.font.inter_variable, FontWeight.Normal),
    Font(R.font.inter_variable, FontWeight.Medium),
    Font(R.font.inter_variable, FontWeight.SemiBold),
    Font(R.font.inter_italic, FontWeight.Normal, style = androidx.compose.ui.text.font.FontStyle.Italic)
)

val DMSerifDisplay = FontFamily(
    Font(R.font.dm_serif_display_regular, FontWeight.Normal),
    Font(R.font.dm_serif_display_italic, FontWeight.Normal, style = androidx.compose.ui.text.font.FontStyle.Italic)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = DMSerifDisplay,
        fontSize = 48.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = DMSerifDisplay,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = DMSerifDisplay,
        fontSize = 26.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    )
)
