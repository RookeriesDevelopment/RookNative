package io.tryrook.rooknative.core.framework.health

import android.app.Activity
import arrow.core.Either
import com.rookmotion.rook.sdk.RookPermissionsManager
import com.rookmotion.rook.sdk.RookStepsManager
import com.rookmotion.rook.sdk.domain.enums.RequestPermissionsStatus
import io.tryrook.rooknative.core.domain.error.HealthError
import io.tryrook.rooknative.core.domain.extension.toEither
import io.tryrook.rooknative.core.framework.error.toHealthError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RookStepsRepository @Inject constructor(
    private val rookPermissionsManager: RookPermissionsManager,
    private val rookStepsManager: RookStepsManager,
) {
    fun checkAndroidPermissions(): Boolean {
        return rookPermissionsManager.checkAndroidPermissions()
    }

    fun shouldRequestAndroidPermissions(activity: Activity): Boolean {
        return RookPermissionsManager.shouldRequestAndroidPermissions(activity)
    }

    fun requestAndroidPermissions(): RequestPermissionsStatus {
        return rookPermissionsManager.requestAndroidPermissions()
    }

    fun isAvailable(): Boolean {
        return rookStepsManager.isAvailable()
    }

    suspend fun isBackgroundAndroidStepsActive(): Boolean {
        return rookStepsManager.isBackgroundAndroidStepsActive()
    }

    fun enableBackgroundAndroidSteps(): Either<HealthError, Unit> {
        return rookStepsManager.enableBackgroundAndroidSteps()
            .toEither { it.toHealthError() }
    }

    fun disableBackgroundAndroidSteps(): Either<HealthError, Unit> {
        return rookStepsManager.disableBackgroundAndroidSteps()
            .toEither { it.toHealthError() }
    }

    suspend fun syncTodayAndroidStepsCount(): Either<HealthError, Long> {
        return rookStepsManager.syncTodayAndroidStepsCount()
            .toEither { it.toHealthError() }
    }
}
