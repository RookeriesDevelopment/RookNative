package io.tryrook.rooknative.feature.login.domain.model

sealed interface LoginAction {
    data class OnUserIDInput(val userID: String) : LoginAction
    data object OnNextClick : LoginAction
}
