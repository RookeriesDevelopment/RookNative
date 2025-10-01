package io.tryrook.rooknative.core.framework.health

import arrow.core.Either
import com.rookmotion.rook.sdk.RookBackgroundSyncManager
import com.rookmotion.rook.sdk.RookConfigurationManager
import com.rookmotion.rook.sdk.RookPermissionsManager
import com.rookmotion.rook.sdk.RookSyncManager
import com.rookmotion.rook.sdk.domain.annotation.ExperimentalRookApi
import com.rookmotion.rook.sdk.domain.enums.BackgroundReadStatus
import com.rookmotion.rook.sdk.domain.enums.HealthConnectAvailability
import com.rookmotion.rook.sdk.domain.enums.RequestPermissionsStatus
import com.rookmotion.rook.sdk.domain.model.DailyCalories
import com.rookmotion.rook.sdk.domain.model.HCActivityEvent
import com.rookmotion.rook.sdk.domain.model.HCBodySummary
import com.rookmotion.rook.sdk.domain.model.HCPhysicalSummary
import com.rookmotion.rook.sdk.domain.model.HCSleepSummary
import com.rookmotion.rook.sdk.domain.model.RookConfiguration
import com.rookmotion.rook.sdk.domain.model.SyncStatusWithData
import com.rookmotion.rook.sdk.domain.model.SyncType
import io.tryrook.rooknative.core.domain.error.HealthError
import io.tryrook.rooknative.core.domain.extension.toEither
import io.tryrook.rooknative.core.framework.error.toHealthError
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RookHealthConnectRepository @Inject constructor(
    private val rookConfigurationManager: RookConfigurationManager,
    private val rookPermissionsManager: RookPermissionsManager,
    private val rookSyncManager: RookSyncManager,
    private val backgroundSyncManager: RookBackgroundSyncManager,
) {
    fun enableLocalLogs() {
        rookConfigurationManager.enableLocalLogs()
    }

    suspend fun getUserID(): String? {
        return rookConfigurationManager.getUserID()
    }

    suspend fun initRook(configuration: RookConfiguration): Either<HealthError, Unit> {
        rookConfigurationManager.setConfiguration(configuration)

        return rookConfigurationManager.initRook()
            .toEither { it.toHealthError() }
    }

    suspend fun updateUserID(userID: String): Either<HealthError, Unit> {
        return rookConfigurationManager.updateUserID(userID)
            .toEither { it.toHealthError() }
    }

    suspend fun syncUserTimeZone(): Either<HealthError, Unit> {
        return rookConfigurationManager.syncUserTimeZone()
            .toEither { it.toHealthError() }
    }

    suspend fun clearUserID(): Either<HealthError, Unit> {
        return rookConfigurationManager.clearUserID()
            .toEither { it.toHealthError() }
    }

    suspend fun deleteUserFromRook(): Either<HealthError, Unit> {
        return rookConfigurationManager.deleteUserFromRook()
            .toEither { it.toHealthError() }
    }

    fun checkHealthConnectAvailability(): HealthConnectAvailability {
        return rookPermissionsManager.checkHealthConnectAvailability()
    }

    fun openHealthConnectSettings(): Either<HealthError, Unit> {
        return rookPermissionsManager.openHealthConnectSettings()
            .toEither { it.toHealthError() }
    }

    suspend fun checkHealthConnectPermissions(): Either<HealthError, Boolean> {
        return rookPermissionsManager.checkHealthConnectPermissions()
            .toEither { it.toHealthError() }
    }

    suspend fun checkHealthConnectPermissionsPartially(): Either<HealthError, Boolean> {
        return rookPermissionsManager.checkHealthConnectPermissionsPartially()
            .toEither { it.toHealthError() }
    }

    suspend fun checkBackgroundReadStatus(): Either<HealthError, BackgroundReadStatus> {
        return rookPermissionsManager.checkBackgroundReadStatus()
            .toEither { it.toHealthError() }
    }

    suspend fun requestHealthConnectPermissions(): Either<HealthError, RequestPermissionsStatus> {
        return rookPermissionsManager.requestHealthConnectPermissions()
            .toEither { it.toHealthError() }
    }

    suspend fun revokeHealthConnectPermissions(): Either<HealthError, Unit> {
        return rookPermissionsManager.revokeHealthConnectPermissions()
            .toEither { it.toHealthError() }
    }

    suspend fun sync(enableLogs: Boolean): Either<HealthError, Unit> {
        return rookSyncManager.sync(enableLogs)
            .map { }
            .toEither { it.toHealthError() }
    }

    suspend fun sync(date: LocalDate): Either<HealthError, Unit> {
        return rookSyncManager.sync(date)
            .map { }
            .toEither { it.toHealthError() }
    }

    suspend fun sync(date: LocalDate, summary: SyncType.Summary): Either<HealthError, Unit> {
        return rookSyncManager.sync(date, summary)
            .map { }
            .toEither { it.toHealthError() }
    }

    suspend fun syncEvents(date: LocalDate, event: SyncType.Event): Either<HealthError, Unit> {
        return rookSyncManager.syncEvents(date, event)
            .map { }
            .toEither { it.toHealthError() }
    }

    suspend fun getSleepSummary(
        date: LocalDate,
    ): Either<HealthError, SyncStatusWithData<List<HCSleepSummary>>> {
        return rookSyncManager.getSleepSummary(date)
            .toEither { it.toHealthError() }
    }

    suspend fun getPhysicalSummary(
        date: LocalDate,
    ): Either<HealthError, SyncStatusWithData<HCPhysicalSummary>> {
        return rookSyncManager.getPhysicalSummary(date)
            .toEither { it.toHealthError() }
    }

    suspend fun getBodySummary(
        date: LocalDate,
    ): Either<HealthError, SyncStatusWithData<HCBodySummary>> {
        return rookSyncManager.getBodySummary(date)
            .toEither { it.toHealthError() }
    }

    suspend fun getActivityEvents(
        date: LocalDate,
    ): Either<HealthError, SyncStatusWithData<List<HCActivityEvent>>> {
        return rookSyncManager.getActivityEvents(date)
            .toEither { it.toHealthError() }
    }

    suspend fun getTodayStepsCount(): Either<HealthError, SyncStatusWithData<Int>> {
        return rookSyncManager.getTodayStepsCount()
            .toEither { it.toHealthError() }
    }

    suspend fun getTodayCaloriesCount(): Either<HealthError, SyncStatusWithData<DailyCalories>> {
        return rookSyncManager.getTodayCaloriesCount()
            .toEither { it.toHealthError() }
    }

    @OptIn(ExperimentalRookApi::class)
    fun isScheduledFlow(): Flow<Boolean> {
        return backgroundSyncManager.isScheduledFlow()
    }

    suspend fun isScheduled(): Either<HealthError, Boolean> {
        return backgroundSyncManager.isScheduled()
            .toEither { it.toHealthError() }
    }

    fun schedule(enableLogs: Boolean, cancelAndReschedule: Boolean = false) {
        backgroundSyncManager.schedule(enableLogs, cancelAndReschedule)
    }

    suspend fun cancel() {
        backgroundSyncManager.cancel()
    }
}
