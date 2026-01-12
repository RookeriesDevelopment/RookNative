package io.tryrook.rooknative.feature.samsunghealth.domain.enums

import io.tryrook.sdk.samsung.domain.enums.SamsungHealthAvailability

enum class SamsungHealthStatus {
    LOADING,
    ERROR,
    NOT_INSTALLED,
    OUTDATED,
    DISABLED,
    NOT_READY,
    READY, ;

    companion object {
        fun fromAvailability(availability: SamsungHealthAvailability): SamsungHealthStatus {
            return when (availability) {
                SamsungHealthAvailability.NOT_INSTALLED -> NOT_INSTALLED
                SamsungHealthAvailability.OUTDATED -> OUTDATED
                SamsungHealthAvailability.DISABLED -> DISABLED
                SamsungHealthAvailability.NOT_READY -> NOT_READY
                SamsungHealthAvailability.INSTALLED -> READY
            }
        }
    }
}
