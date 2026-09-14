package ir.atrium.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AtriumColors(
    val brand: Color = Color(0xFF39AB6C),
    val onBrand: Color = Color.White,
    val background: Color = Color(0xFFF8F9FA),
    val onBackground: Color = Color(0xFF111418),
    val surface: Color = Color.White,
    val surfaceHighlight: Color = Color(0xFFF0F2F5),
    val onSurface: Color = Color(0xFF1A1D21),
    val onSurfaceMuted: Color = Color(0xFF6B7280),
    val outline: Color = Color(0xFFE5E7EB),
    val outlineFaint: Color = Color(0xFFF3F4F6),
    val success: Color = Color(0xFF10B981),
    val warning: Color = Color(0xFFF59E0B),
    val error: Color = Color(0xFFEF4444),
    val onError: Color = Color.White,
    val evidenceVerified: Color = Color(0xFF059669),
    val officialBusiness: Color = Color(0xFF2563EB),
    val reputation: Color = Color(0xFF8B5CF6),
    val scrim: Color = Color.Black.copy(alpha = 0.32f),
)

val LocalAtriumColors = staticCompositionLocalOf { AtriumColors() }
