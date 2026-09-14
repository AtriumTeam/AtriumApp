package ir.atrium.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ir.atrium.core.designsystem.R

val VazirmatnFamily = FontFamily(
    Font(R.font.vazirmatn_regular, FontWeight.Normal),
    Font(R.font.vazirmatn_medium, FontWeight.Medium),
    Font(R.font.vazirmatn_semibold, FontWeight.SemiBold),
    Font(R.font.vazirmatn_bold, FontWeight.Bold),
)

val AtriumSignFamily = FontFamily(
    Font(R.font.atrium_sign_bold, FontWeight.Bold),
)

@Immutable
data class AtriumTypography(
    val display: TextStyle = TextStyle(fontFamily = VazirmatnFamily, fontWeight = FontWeight.Bold, fontSize = 40.sp),
    val h1: TextStyle = TextStyle(fontFamily = VazirmatnFamily, fontWeight = FontWeight.Bold, fontSize = 32.sp),
    val h2: TextStyle = TextStyle(fontFamily = VazirmatnFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp),
    val h3: TextStyle = TextStyle(fontFamily = VazirmatnFamily, fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
    val title: TextStyle = TextStyle(fontFamily = VazirmatnFamily, fontWeight = FontWeight.SemiBold, fontSize = 17.sp),
    val bodyLarge: TextStyle = TextStyle(fontFamily = VazirmatnFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp),
    val body: TextStyle = TextStyle(fontFamily = VazirmatnFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp),
    val label: TextStyle = TextStyle(fontFamily = VazirmatnFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    val caption: TextStyle = TextStyle(fontFamily = VazirmatnFamily, fontWeight = FontWeight.Medium, fontSize = 12.sp),
    val micro: TextStyle = TextStyle(fontFamily = VazirmatnFamily, fontWeight = FontWeight.Medium, fontSize = 10.sp),
)

val LocalAtriumTypography = staticCompositionLocalOf { AtriumTypography() }
