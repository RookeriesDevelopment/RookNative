package io.tryrook.rooknative.core.framework.health

import com.rookmotion.rook.sdk.RookBackgroundSyncManager
import com.rookmotion.rook.sdk.RookDataSources
import com.rookmotion.rook.sdk.RookStepsManager
import io.tryrook.rooknative.core.domain.repository.HealthRepository
import io.tryrook.rooknative.core.framework.extension.isAtLeastOneConnected
import io.tryrook.sdk.samsung.RookSamsung

class RookHealthRepository(
    private val backgroundSyncManager: RookBackgroundSyncManager,
    private val stepsManager: RookStepsManager,
    private val rookDataSources: RookDataSources,
    private val rookSamsung: RookSamsung,
) : HealthRepository {
    override suspend fun isHealthConnectEnabled(): Boolean {
        return backgroundSyncManager.isScheduled().getOrDefault(defaultValue = false)
    }

    override suspend fun isAndroidEnabled(): Boolean {
        return stepsManager.isBackgroundAndroidStepsActive()
    }

    override suspend fun isApiEnabled(): Boolean {
        val authorizedDataSources = rookDataSources.getAuthorizedDataSources().getOrNull()

        return authorizedDataSources?.isAtLeastOneConnected() == true
    }

    override suspend fun isSamsungHealthEnabled(): Boolean {
        return rookSamsung.isScheduled().getOrDefault(defaultValue = false)
    }

}
