package com.bangkit.nest.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bangkit.nest.R


val urbanistFontFamily = FontFamily(
    Font(R.font.urbanist_regular),
    Font(R.font.urbanist_medium),
    Font(R.font.urbanist_semibold, FontWeight.W600),
    Font(R.font.urbanist_bold, FontWeight.W700)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = urbanistFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 24.sp,
    )
)

val h1TextStyle = TextStyle(
    fontSize = 30.sp,
    fontFamily = urbanistFontFamily,
    fontWeight = FontWeight.Bold
)

val h2TextStyle = TextStyle(
    fontSize = 24.sp,
    fontFamily = urbanistFontFamily,
    fontWeight = FontWeight.Bold
)

val h3TextStyle = TextStyle(
    fontSize = 20.sp,
    fontFamily = urbanistFontFamily,
    fontWeight = FontWeight.Bold
)

val subTitleFormStyle = TextStyle(
    fontSize = 16.sp,
    fontFamily = urbanistFontFamily,
    fontWeight = FontWeight.SemiBold
)

val bodyTextStyle = TextStyle(
    fontSize = 14.sp,
    fontFamily = urbanistFontFamily
)

val bodyTextStyleMedium = TextStyle(
    fontSize = 14.sp,
    fontFamily = urbanistFontFamily,
    fontWeight = FontWeight.Medium

)


