package io.tryrook.rooknative.core.framework.health

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import arrow.core.Either
import io.tryrook.rooknative.core.domain.error.HealthError
import io.tryrook.rooknative.core.domain.extension.toEither
import io.tryrook.rooknative.core.framework.error.toHealthError
import io.tryrook.sdk.samsung.RookSamsung
import io.tryrook.sdk.samsung.domain.annotation.ExperimentalRookSamsungApi
import io.tryrook.sdk.samsung.domain.enums.SHRequestPermissionsStatus
import io.tryrook.sdk.samsung.domain.enums.SamsungHealthAvailability
import io.tryrook.sdk.samsung.domain.enums.SamsungHealthPermission
import io.tryrook.sdk.samsung.domain.model.SHActivityEvent
import io.tryrook.sdk.samsung.domain.model.SHBodySummary
import io.tryrook.sdk.samsung.domain.model.SHCalories
import io.tryrook.sdk.samsung.domain.model.SHConfiguration
import io.tryrook.sdk.samsung.domain.model.SHPhysicalSummary
import io.tryrook.sdk.samsung.domain.model.SHSleepSummary
import io.tryrook.sdk.samsung.domain.model.SHSyncStatusWithData
import io.tryrook.sdk.samsung.domain.model.SHSyncType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RookSamsungHealthRepository @Inject constructor(
    private val rookSamsung: RookSamsung,
) {
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    fun isCompatible(): Boolean {
        // On version 1.1.0 or lower samsung health is compatible with SDK 26 (Android 8 - Build.VERSION_CODES.O)
        // Future versions will only support SDK 29+ (Android 10 - Build.VERSION_CODES.Q). https://developer.samsung.com/health/data/overview.html#Restrictions
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    fun enableLocalLogs() {
        rookSamsung.enableLocalLogs()
    }

    suspend fun getUserID(): String? {
        return rookSamsung.getUserID()
    }

    suspend fun initRook(configuration: SHConfiguration): Either<HealthError, Unit> {
        return rookSamsung.initRook(configuration)
            .toEither { it.toHealthError() }
    }

    suspend fun updateUserID(userID: String): Either<HealthError, Unit> {
        return rookSamsung.updateUserID(userID)
            .toEither { it.toHealthError() }
    }

    suspend fun syncUserTimeZone(): Either<HealthError, Unit> {
        return rookSamsung.syncUserTimeZone()
            .toEither { it.toHealthError() }
    }

    suspend fun clearUserID(): Either<HealthError, Unit> {
        return rookSamsung.clearUserID()
            .toEither { it.toHealthError() }
    }

    suspend fun deleteUserFromRook(): Either<HealthError, Unit> {
        return rookSamsung.deleteUserFromRook()
            .toEither { it.toHealthError() }
    }

    suspend fun checkSamsungHealthAvailability(): Either<HealthError, SamsungHealthAvailability> {
        return rookSamsung.checkSamsungHealthAvailability()
            .toEither { it.toHealthError() }
    }

    suspend fun checkSamsungHealthPermissions(
        permissions: Set<SamsungHealthPermission>,
    ): Either<HealthError, Boolean> {
        return rookSamsung.checkSamsungHealthPermissions(permissions)
            .toEither { it.toHealthError() }
    }

    suspend fun checkSamsungHealthPermissionsPartially(
        permissions: Set<SamsungHealthPermission>,
    ): Either<HealthError, Boolean> {
        return rookSamsung.checkSamsungHealthPermissionsPartially(permissions)
            .toEither { it.toHealthError() }
    }

    suspend fun requestSamsungHealthPermissions(
        permissions: Set<SamsungHealthPermission>,
    ): Either<HealthError, SHRequestPermissionsStatus> {
        return rookSamsung.requestSamsungHealthPermissions(permissions)
            .toEither { it.toHealthError() }
    }

    suspend fun sync(enableLogs: Boolean): Either<HealthError, Unit> {
        return rookSamsung.sync(enableLogs)
            .map { }
            .toEither { it.toHealthError() }
    }

    suspend fun sync(date: LocalDate): Either<HealthError, Unit> {
        return rookSamsung.sync(date)
            .map { }
            .toEither { it.toHealthError() }
    }

    suspend fun sync(date: LocalDate, summary: SHSyncType.Summary): Either<HealthError, Unit> {
        return rookSamsung.sync(date, summary)
            .map { }
            .toEither { it.toHealthError() }
    }

    suspend fun syncEvents(date: LocalDate, event: SHSyncType.Event): Either<HealthError, Unit> {
        return rookSamsung.syncEvents(date, event)
            .map { }
            .toEither { it.toHealthError() }
    }

    suspend fun getSleepSummary(
        date: LocalDate,
    ): Either<HealthError, SHSyncStatusWithData<List<SHSleepSummary>>> {
        return rookSamsung.getSleepSummary(date)
            .toEither { it.toHealthError() }
    }

    suspend fun getPhysicalSummary(
        date: LocalDate,
    ): Either<HealthError, SHSyncStatusWithData<SHPhysicalSummary>> {
        return rookSamsung.getPhysicalSummary(date)
            .toEither { it.toHealthError() }
    }

    suspend fun getBodySummary(
        date: LocalDate,
    ): Either<HealthError, SHSyncStatusWithData<SHBodySummary>> {
        return rookSamsung.getBodySummary(date)
            .toEither { it.toHealthError() }
    }

    suspend fun getActivityEvents(
        date: LocalDate,
    ): Either<HealthError, SHSyncStatusWithData<List<SHActivityEvent>>> {
        return rookSamsung.getActivityEvents(date)
            .toEither { it.toHealthError() }
    }

    suspend fun getTodayStepsCount(): Either<HealthError, SHSyncStatusWithData<Int>> {
        return rookSamsung.getTodayStepsCount()
            .toEither { it.toHealthError() }
    }

    suspend fun getTodayCaloriesCount(): Either<HealthError, SHSyncStatusWithData<SHCalories>> {
        return rookSamsung.getTodayCaloriesCount()
            .toEither { it.toHealthError() }
    }

    @OptIn(ExperimentalRookSamsungApi::class)
    fun isScheduledFlow(): Flow<Boolean> {
        return rookSamsung.isScheduledFlow()
    }

    suspend fun isScheduled(): Either<HealthError, Boolean> {
        return rookSamsung.isScheduled()
            .toEither { it.toHealthError() }
    }

    fun schedule(enableLogs: Boolean, cancelAndReschedule: Boolean = false) {
        rookSamsung.schedule(enableLogs, cancelAndReschedule)
    }

    suspend fun cancel() {
        rookSamsung.cancel()
    }
}
