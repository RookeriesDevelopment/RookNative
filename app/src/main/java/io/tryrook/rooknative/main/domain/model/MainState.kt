package io.tryrook.rooknative.main.domain.model

import io.tryrook.rooknative.main.domain.enums.UserSessionStatus

data class MainState(
    val loading: Boolean = true,
    val userSessionStatus: UserSessionStatus = UserSessionStatus.NONE,
)