package com.haotsang.wanandroidkmp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.haotsang.wanandroidkmp.getKvStorage

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

class ThemeManager {
    private val kvStorage = getKvStorage()
    private val THEME_KEY = "app_theme"

    private val _themeMode = mutableStateOf(getSavedTheme())
    val themeMode: State<ThemeMode> = _themeMode

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        kvStorage.saveString(THEME_KEY, mode.name)
    }

    private fun getSavedTheme(): ThemeMode {
        val savedTheme = kvStorage.getString(THEME_KEY)
        return try {
            savedTheme?.let { ThemeMode.valueOf(it) } ?: ThemeMode.SYSTEM
        } catch (e: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
    }
}

@Composable
fun rememberThemeMode(): MutableState<ThemeMode> {
    val themeManager = ThemeManager()
    val themeMode = rememberSaveable {
        mutableStateOf(themeManager.themeMode.value)
    }
    return themeMode
}