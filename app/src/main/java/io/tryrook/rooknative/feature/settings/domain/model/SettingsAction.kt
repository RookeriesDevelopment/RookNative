package io.tryrook.rooknative.feature.settings.domain.model

sealed interface SettingsAction {
    data object OnLogoutClick : SettingsAction
}
