package io.tryrook.rooknative.feature.connections.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.tryrook.rooknative.R

sealed interface Connection {
    data class Api(val name: String, val connected: Boolean, val iconUrl: String) : Connection
    data class HealthKit(@StringRes val nameRes: Int, @DrawableRes val iconRes: Int) : Connection {
        companion object {
            fun buildHealthConnect(): HealthKit {
                return HealthKit(
                    nameRes = R.string.health_connect,
                    iconRes = R.drawable.svg_health_connect,
                )
            }

            fun buildSamsungHealth(): HealthKit {
                return HealthKit(
                    nameRes = R.string.samsung_health,
                    iconRes = R.drawable.png_samsung_health,
                )
            }

            fun buildAndroid(): HealthKit {
                return HealthKit(
                    nameRes = R.string.android,
                    iconRes = R.drawable.svg_android,
                )
            }
        }
    }
}
