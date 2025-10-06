package io.tryrook.rooknative.feature.samsunghealth.domain.model

data class SamsungHealthState(
    val samsungStatus: SamsungStatus = SamsungStatus.Loading,
    val permissionsGranted: Boolean = false,
    val showAllowAccessButton: Boolean = true,
)
