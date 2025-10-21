package io.tryrook.rooknative.feature.connections.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.rookmotion.rook.sdk.domain.enums.DataSourceType
import io.tryrook.rooknative.R

sealed interface Connection {
    data class Api(val name: String, val connected: Boolean, val iconUrl: String) : Connection {
        val disconnectionType: DataSourceType?
            get() {
                return when (name) {
                    "Oura" -> DataSourceType.OURA
                    "Polar" -> DataSourceType.POLAR
                    "Whoop" -> DataSourceType.WHOOP
                    "Fitbit" -> DataSourceType.FITBIT
                    "Garmin" -> DataSourceType.GARMIN
                    "Withings" -> DataSourceType.WITHINGS
                    else -> null
                }
            }
    }

    data class HealthKit(
        val healthKitType: HealthKitType,
        @StringRes val nameRes: Int,
        val connected: Boolean,
        @DrawableRes val iconRes: Int = R.drawable.svg_health_connect,
    ) : Connection {
        companion object {
            fun buildHealthConnect(connected: Boolean): HealthKit {
                return HealthKit(
                    healthKitType = HealthKitType.HEALTH_CONNECT,
                    nameRes = R.string.health_connect,
                    connected = connected,
                    iconRes = R.drawable.svg_health_connect,
                )
            }

            fun buildSamsungHealth(connected: Boolean): HealthKit {
                return HealthKit(
                    healthKitType = HealthKitType.SAMSUNG_HEALTH,
                    nameRes = R.string.samsung_health,
                    connected = connected,
                    iconRes = R.drawable.png_samsung_health,
                )
            }

            fun buildAndroidSteps(connected: Boolean): HealthKit {
                return HealthKit(
                    healthKitType = HealthKitType.ANDROID_STEPS,
                    nameRes = R.string.android,
                    connected = connected,
                    iconRes = R.drawable.svg_android,
                )
            }
        }
    }
}

enum class HealthKitType {
    HEALTH_CONNECT,
    SAMSUNG_HEALTH,
    ANDROID_STEPS, ;
}
