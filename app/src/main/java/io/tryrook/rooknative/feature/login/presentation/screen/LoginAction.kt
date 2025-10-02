package io.tryrook.rooknative.feature.login.presentation.screen

sealed interface LoginAction {
    data class OnUserIDInput(val userID: String) : LoginAction
    data object OnNextClick : LoginAction
}
