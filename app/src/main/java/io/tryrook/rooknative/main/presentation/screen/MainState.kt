package io.tryrook.rooknative.main.presentation.screen

import io.tryrook.rooknative.main.domain.enums.UserSessionStatus

data class MainState(
    val loading: Boolean = true,
    val userSessionStatus: UserSessionStatus = UserSessionStatus.NONE,
)
