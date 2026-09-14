package ir.atrium.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class AtriumShapes(
    val small: RoundedCornerShape = RoundedCornerShape(8.dp),
    val medium: RoundedCornerShape = RoundedCornerShape(16.dp),
    val large: RoundedCornerShape = RoundedCornerShape(24.dp),
    val asymmetricFolio: RoundedCornerShape = RoundedCornerShape(topStart = 24.dp, topEnd = 6.dp, bottomEnd = 24.dp, bottomStart = 6.dp),
    val sheetTop: RoundedCornerShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
)

val LocalAtriumShapes = staticCompositionLocalOf { AtriumShapes() }
