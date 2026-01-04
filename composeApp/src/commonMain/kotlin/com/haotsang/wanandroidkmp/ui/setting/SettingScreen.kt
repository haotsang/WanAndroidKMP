package com.haotsang.wanandroidkmp.ui.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.haotsang.wanandroidkmp.ui.common.WanTopAppBar
import com.haotsang.wanandroidkmp.ui.theme.ThemeManager
import com.haotsang.wanandroidkmp.ui.theme.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(onBack: () -> Unit) {
    val themeManager = remember { ThemeManager() }
    val themeMode = themeManager.themeMode

    Scaffold(
        topBar = {
            WanTopAppBar(title = "设置", onBackClick = onBack)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 主题设置
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "主题设置",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )

                ThemeOption(
                label = "跟随系统",
                selected = themeMode.value == ThemeMode.SYSTEM,
                onSelect = {
                    themeManager.setThemeMode(ThemeMode.SYSTEM)
                }
            )

            ThemeOption(
                label = "浅色模式",
                selected = themeMode.value == ThemeMode.LIGHT,
                onSelect = {
                    themeManager.setThemeMode(ThemeMode.LIGHT)
                }
            )

            ThemeOption(
                label = "深色模式",
                selected = themeMode.value == ThemeMode.DARK,
                onSelect = {
                    themeManager.setThemeMode(ThemeMode.DARK)
                }
            )
            }
        }
    }
}

@Composable
private fun ThemeOption(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .clickable { onSelect() }
        )
    }
}