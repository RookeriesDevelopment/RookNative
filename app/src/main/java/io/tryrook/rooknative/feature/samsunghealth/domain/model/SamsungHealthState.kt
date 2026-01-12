package io.tryrook.rooknative.feature.samsunghealth.domain.model

import io.tryrook.rooknative.feature.samsunghealth.domain.enums.SamsungHealthStatus

data class SamsungHealthState(
    val samsungHealthStatus: SamsungHealthStatus = SamsungHealthStatus.LOADING,
    val permissionsGranted: Boolean = false,
    val showAllowAccessButton: Boolean = true,
)
