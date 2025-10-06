package io.tryrook.rooknative.feature.samsunghealth.domain.model

import io.tryrook.sdk.samsung.domain.enums.SamsungHealthAvailability

sealed interface SamsungStatus {
    data object Loading : SamsungStatus
    data object Error : SamsungStatus
    data class Loaded(val availability: SamsungHealthAvailability) : SamsungStatus
}
