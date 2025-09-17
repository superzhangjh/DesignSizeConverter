package com.max.designconvertor

import DesignSizeConverterApp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.russhwolf.settings.PreferencesSettings

@Composable
@Preview
fun App() {
    val setting = PreferencesSettings.Factory().create()
    MaterialTheme {
        DesignSizeConverterApp(setting)
    }
}