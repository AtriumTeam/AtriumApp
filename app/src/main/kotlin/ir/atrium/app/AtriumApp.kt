package ir.atrium.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.atrium.core.designsystem.theme.AtriumTheme

@Composable
fun AtriumApp() {
    AtriumTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AtriumTheme.colors.background),
        )
    }
}
