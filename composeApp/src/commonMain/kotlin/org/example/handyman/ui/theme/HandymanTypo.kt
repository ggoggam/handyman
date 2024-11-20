package org.example.handyman.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import handyman.composeapp.generated.resources.Res
import handyman.composeapp.generated.resources.pretendard_black
import handyman.composeapp.generated.resources.pretendard_bold
import handyman.composeapp.generated.resources.pretendard_extrabold
import handyman.composeapp.generated.resources.pretendard_extralight
import handyman.composeapp.generated.resources.pretendard_regular
import handyman.composeapp.generated.resources.pretendard_semibold
import handyman.composeapp.generated.resources.pretendard_thin
import org.jetbrains.compose.resources.Font

@Composable
fun PretendardFontFamily() = FontFamily(
    Font(Res.font.pretendard_extralight, FontWeight.ExtraLight),
    Font(Res.font.pretendard_thin, FontWeight.Thin),
    Font(Res.font.pretendard_regular, FontWeight.Normal),
    Font(Res.font.pretendard_semibold, FontWeight.SemiBold),
    Font(Res.font.pretendard_bold, FontWeight.Bold),
    Font(Res.font.pretendard_extrabold, FontWeight.ExtraBold),
    Font(Res.font.pretendard_black, FontWeight.Black)
)


@Composable
fun handymanTypo() = Typography().run {
    val fontFamily = PretendardFontFamily()
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}