package io.tryrook.rooknative.feature.settings.presentation.screen

sealed interface SettingsAction {
    data object OnLogoutClick : SettingsAction
}
