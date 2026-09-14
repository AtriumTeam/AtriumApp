package ir.atrium.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun AtriumTheme(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAtriumColors provides AtriumColors(),
        LocalAtriumTypography provides AtriumTypography(),
        LocalAtriumShapes provides AtriumShapes(),
        content = content,
    )
}

object AtriumTheme {
    val colors: AtriumColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAtriumColors.current

    val typography: AtriumTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAtriumTypography.current

    val shapes: AtriumShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalAtriumShapes.current
}
