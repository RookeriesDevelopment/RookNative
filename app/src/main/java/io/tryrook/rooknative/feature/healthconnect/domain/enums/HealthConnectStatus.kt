package io.tryrook.rooknative.feature.healthconnect.domain.enums

import com.rookmotion.rook.sdk.domain.enums.BackgroundReadStatus
import com.rookmotion.rook.sdk.domain.enums.HealthConnectAvailability

enum class HealthConnectStatus {
    LOADING,
    NOT_SUPPORTED,
    NOT_INSTALLED,
    BACKGROUND_NOT_SUPPORTED,
    READY, ;

    companion object {
        fun fromAvailabilityAndBackgroundStatus(
            availability: HealthConnectAvailability,
            backgroundStatus: BackgroundReadStatus,
        ): HealthConnectStatus {
            if (availability == HealthConnectAvailability.NOT_SUPPORTED) {
                return NOT_SUPPORTED
            }

            if (availability == HealthConnectAvailability.NOT_INSTALLED) {
                return NOT_INSTALLED
            }

            // Installed but background read is not supported
            if (backgroundStatus == BackgroundReadStatus.UNAVAILABLE) {
                return BACKGROUND_NOT_SUPPORTED
            }

            // Installed and background read is supported but could not be granted
            return READY
        }
    }
}
