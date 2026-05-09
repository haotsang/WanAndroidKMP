package com.haotsang.wanandroidkmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.haotsang.wanandroidkmp.ui.MainRoute
import com.haotsang.wanandroidkmp.ui.theme.ThemeMode
import org.jetbrains.compose.ui.tooling.preview.Preview

private val LightColorScheme = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF007AFF),
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = androidx.compose.ui.graphics.Color(0xFFE5F1FF),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF001E3C),
    secondary = androidx.compose.ui.graphics.Color(0xFF5856D6),
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFFE8E7FF),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFF151244),
    background = androidx.compose.ui.graphics.Color(0xFFFAFAFA),
    onBackground = androidx.compose.ui.graphics.Color(0xFF1C1C1E),
    surface = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    onSurface = androidx.compose.ui.graphics.Color(0xFF1C1C1E),
    error = androidx.compose.ui.graphics.Color(0xFFFF3B30),
    onError = androidx.compose.ui.graphics.Color.White,
    errorContainer = androidx.compose.ui.graphics.Color(0xFFFFE5E5),
    onErrorContainer = androidx.compose.ui.graphics.Color(0xFF410002),
    outline = androidx.compose.ui.graphics.Color(0xFFC7C7CC),
    outlineVariant = androidx.compose.ui.graphics.Color(0xFFE5E5EA),
    scrim = androidx.compose.ui.graphics.Color(0xFF000000)
)

private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF007AFF),
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = androidx.compose.ui.graphics.Color(0xFF001E3C),
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFFE5F1FF),
    secondary = androidx.compose.ui.graphics.Color(0xFF5856D6),
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFF151244),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFFE8E7FF),
    background = androidx.compose.ui.graphics.Color(0xFF1C1C1E),
    onBackground = androidx.compose.ui.graphics.Color(0xFFF2F2F7),
    surface = androidx.compose.ui.graphics.Color(0xFF2C2C2E),
    onSurface = androidx.compose.ui.graphics.Color(0xFFF2F2F7),
    error = androidx.compose.ui.graphics.Color(0xFFFF453A),
    onError = androidx.compose.ui.graphics.Color.White,
    errorContainer = androidx.compose.ui.graphics.Color(0xFF690005),
    onErrorContainer = androidx.compose.ui.graphics.Color(0xFFFFDAD6),
    outline = androidx.compose.ui.graphics.Color(0xFF545458),
    outlineVariant = androidx.compose.ui.graphics.Color(0xFF38383A),
    scrim = androidx.compose.ui.graphics.Color(0xFF000000)
)

@Composable
fun App(useNavRail: Boolean = false, themeMode: ThemeMode = ThemeMode.SYSTEM) {
    MaterialTheme(
        colorScheme = when (themeMode) {
            ThemeMode.LIGHT -> LightColorScheme
            ThemeMode.DARK -> DarkColorScheme
            ThemeMode.SYSTEM -> {
                // 在实际应用中，这里应该根据系统主题选择，
                // 但在预览环境中，我们使用亮色主题
                LightColorScheme
            }
        },
        typography = androidx.compose.material3.Typography()
    ) {
        MainRoute(useNavRail)
    }
}

@Preview
@Composable
fun AppPreview() {
    App(useNavRail = false, themeMode = ThemeMode.LIGHT)
}

@Preview
@Composable
fun AppDarkPreview() {
    App(useNavRail = false, themeMode = ThemeMode.DARK)
}