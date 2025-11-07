package io.tryrook.rooknative.feature.settings.domain.model

sealed interface SettingsEvent {
    data object Logout : SettingsEvent
    data object LogoutError : SettingsEvent
}
