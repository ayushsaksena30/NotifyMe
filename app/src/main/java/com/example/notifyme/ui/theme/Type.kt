package com.example.notifyme.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.notifyme.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val clashDisplay = FontFamily(
    Font(R.font.clashdisplay_light, FontWeight.Light),
    Font(R.font.clashdisplay_bold, FontWeight.Bold),
    Font(R.font.clashdisplay_medium, FontWeight.Medium),
    Font(R.font.clashdisplay_regular, FontWeight.Normal),
    Font(R.font.clashdisplay_semibold, FontWeight.SemiBold),
    Font(R.font.clashdisplay_extralight, FontWeight.ExtraLight)
)

val latoFontFamily = FontFamily(
    Font(R.font.lato_light, FontWeight.Light),
    Font(R.font.lato_bold, FontWeight.Bold),
    Font(R.font.lato_thin, FontWeight.Thin),
    Font(R.font.lato_regular, FontWeight.Normal),
    Font(R.font.lato_black, FontWeight.Black)
)