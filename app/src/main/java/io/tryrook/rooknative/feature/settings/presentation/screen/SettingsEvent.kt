package io.tryrook.rooknative.feature.settings.presentation.screen

sealed interface SettingsEvent {
    data object Logout : SettingsEvent
    data object LogoutError : SettingsEvent
}
