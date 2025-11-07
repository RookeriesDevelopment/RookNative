package io.tryrook.rooknative.feature.login.domain.model

import io.tryrook.rooknative.core.presentation.text.UIText

sealed interface LoginEvent {
    data class Error(val message: UIText) : LoginEvent
    data object LoggedIn : LoginEvent
}
