package io.tryrook.rooknative.feature.healthconnect.domain.model

import io.tryrook.rooknative.feature.healthconnect.domain.enums.HealthConnectStatus

data class HealthConnectState(
    val healthConnectStatus: HealthConnectStatus = HealthConnectStatus.LOADING,
    val dataTypesGranted: Boolean = false,
    val backgroundGranted: Boolean = false,
    val showAllowAccessButton: Boolean = true,
    val showOpenSettingsButton: Boolean = false,
)
