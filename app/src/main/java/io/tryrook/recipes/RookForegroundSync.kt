package io.tryrook.recipes

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.rookmotion.rook.sdk.RookPermissionsManager
import com.rookmotion.rook.sdk.domain.annotation.ExplicitThrow
import com.rookmotion.rook.sdk.domain.environment.RookEnvironment
import com.rookmotion.rook.sdk.domain.model.RookConfiguration
import com.rookmotion.rook.sdk.domain.model.SyncType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.tryrook.rooknative.BuildConfig
import io.tryrook.rooknative.core.framework.health.RookHealthConnectRepository
import timber.log.Timber
import java.time.LocalDate
import java.time.Period
import java.util.concurrent.TimeUnit

@SuppressLint("MissingPermission")
suspend fun usage(context: Context) {
    // We assume that Rook, Work Manager and Work manager Hilt's extension are already configured.

    // Check Android permissions
    val hasAndroidPermissions = checkRookForegroundSyncAndroidPermissions(context)

    if (!hasAndroidPermissions) {
        return
    }

    // Check Health Connect permissions
    val healthConnectPermissions = RookPermissionsManager
        .checkHealthConnectPermissions(context)
        .getOrDefault(defaultValue = false)
    val healthConnectPartialPermissions = RookPermissionsManager
        .checkHealthConnectPermissionsPartially(context)
        .getOrDefault(defaultValue = false)

    if (!healthConnectPermissions && !healthConnectPartialPermissions) {
        return
    }

    RookForegroundSync.syncNow(context)
}

@HiltWorker
class RookForegroundSync @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val healthConnectRepository: RookHealthConnectRepository,
    private val syncRegistryRepository: SyncRegistryRepository,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return buildForegroundInfo(applicationContext)
    }

    @OptIn(ExplicitThrow::class)
    override suspend fun doWork(): Result {
        setForeground(buildForegroundInfo(applicationContext))

        if (BuildConfig.DEBUG) {
            healthConnectRepository.enableLocalLogs()
        }

        val configuration = RookConfiguration(
            clientUUID = BuildConfig.CLIENT_UUID,
            secretKey = BuildConfig.SECRET_KEY,
            environment = RookEnvironment.SANDBOX,
        )

        val initialized = healthConnectRepository.initRook(configuration).isRight()

        if (!initialized) {
            return Result.failure()
        }

        val userID = healthConnectRepository.getUserID()

        if (userID == null) {
            return Result.failure()
        }

        syncTodaySummaries()
        syncTodayEvents()
        syncHistoricSummariesAndActivity()

        val twentyNineDays = LocalDate.now().minus(TWENTY_NINE_DAYS)

        syncRegistryRepository.clearOlderThan(twentyNineDays)

        return Result.success()
    }

    @ExplicitThrow
    private suspend fun syncTodaySummaries() {
        Timber.i("Syncing today summaries...")

        val today = LocalDate.now()

        // Only Sleep Summaries allow syncing the current day (to get the the sleep sessions of yesterday to today).

        healthConnectRepository.sync(today, SyncType.Summary.SLEEP).fold(
            {
                Timber.i("Finished syncing $today SLEEP_SUMMARY!")
            },
            {
                Timber.e("Failed to sync $today SLEEP_SUMMARY with error: $it")
            },
        )
    }

    @ExplicitThrow
    private suspend fun syncTodayEvents() {
        Timber.i("Syncing today events...")

        val today = LocalDate.now()

        healthConnectRepository.syncEvents(today, SyncType.Event.ACTIVITY).fold(
            {
                Timber.i("Finished syncing $today PHYSICAL_EVENT!")
            },
            {
                Timber.e("Failed to sync $today PHYSICAL_EVENT with error: $it")
            },
        )

        healthConnectRepository.syncEvents(today, SyncType.Event.STEPS).fold(
            {
                Timber.i("Finished syncing $today STEPS_EVENT!")
            },
            {
                Timber.e("Failed to sync $today STEPS_EVENT with error: $it")
            },
        )

        healthConnectRepository.syncEvents(today, SyncType.Event.CALORIES).fold(
            {
                Timber.i("Finished syncing $today CALORIES_EVENT!")
            },
            {
                Timber.e("Failed to sync $today CALORIES_EVENT with error: $it")
            },
        )

        // Skipped to save Health Connect requests quota, customize to your requirements

//        healthConnectRepository.syncEvents(today, SyncType.Event.BODY_METRICS).fold(
//            {
//                Timber.i("Finished syncing $today BODY_METRICS_EVENT!")
//            },
//            {
//                Timber.e("Failed to sync $today BODY_METRICS_EVENT with error: $it")
//            },
//        )
//
//        healthConnectRepository.syncEvents(today, SyncType.Event.HEART_RATE).fold(
//            {
//                Timber.i("Finished syncing $today HEART_RATE_EVENT!")
//            },
//            {
//                Timber.e("Failed to sync $today HEART_RATE_EVENT with error: $it")
//            },
//        )
//
//        healthConnectRepository.syncEvents(today, SyncType.Event.OXYGENATION).fold(
//            {
//                Timber.i("Finished syncing $today OXYGENATION_EVENT!")
//            },
//            {
//                Timber.e("Failed to sync $today OXYGENATION_EVENT with error: $it")
//            },
//        )
//
//        healthConnectRepository.syncEvents(today, SyncType.Event.TEMPERATURE).fold(
//            {
//                Timber.i("Finished syncing $today TEMPERATURE_EVENT!")
//            },
//            {
//                Timber.e("Failed to sync $today TEMPERATURE_EVENT with error: $it")
//            },
//        )
//
//        healthConnectRepository.syncEvents(today, SyncType.Event.HYDRATION).fold(
//            {
//                Timber.i("Finished syncing $today HYDRATION_EVENT!")
//            },
//            {
//                Timber.e("Failed to sync $today HYDRATION_EVENT with error: $it")
//            },
//        )
//
//        healthConnectRepository.syncEvents(today, SyncType.Event.NUTRITION).fold(
//            {
//                Timber.i("Finished syncing $today NUTRITION_EVENT!")
//            },
//            {
//                Timber.e("Failed to sync $today NUTRITION_EVENT with error: $it")
//            },
//        )
//
//        healthConnectRepository.syncEvents(today, SyncType.Event.BLOOD_PRESSURE).fold(
//            {
//                Timber.i("Finished syncing $today BLOOD_PRESSURE_EVENT!")
//            },
//            {
//                Timber.e("Failed to sync $today BLOOD_PRESSURE_EVENT with error: $it")
//            },
//        )
//
//        healthConnectRepository.syncEvents(today, SyncType.Event.BLOOD_GLUCOSE).fold(
//            {
//                Timber.i("Finished syncing $today BLOOD_GLUCOSE_EVENT!")
//            },
//            {
//                Timber.e("Failed to sync $today BLOOD_GLUCOSE_EVENT with error: $it")
//            },
//        )
    }

    @ExplicitThrow
    private suspend fun syncHistoricSummariesAndActivity() {
        Timber.i("Syncing historic summaries and activity...")

        val today = LocalDate.now()
        val dateOverflow = today.minus(THIRTY_DAYS)
        var currentDay = today.minus(ONE_DAY)

        while (currentDay.isAfter(dateOverflow)) {
            syncHealthData(currentDay)

            currentDay = currentDay.minus(ONE_DAY)
        }
    }

    @ExplicitThrow
    private suspend fun syncHealthData(selectedDate: LocalDate) {
        syncRegistryRepository.getByTypeAndDate(
            type = SyncRegistryType.SLEEP_SUMMARY,
            date = selectedDate,
        ).let { syncRegistry ->
            if (syncRegistry != null) {
                Timber.i("SLEEP_SUMMARY for $selectedDate was already synced")
                return@let
            }

            healthConnectRepository.sync(selectedDate, SyncType.Summary.SLEEP).fold(
                {
                    Timber.i("Finished syncing $selectedDate SLEEP_SUMMARY!")

                    syncRegistryRepository.insertOrReplace(
                        SyncRegistryType.SLEEP_SUMMARY,
                        selectedDate
                    )
                },
                {
                    Timber.e("Failed to sync $selectedDate SLEEP_SUMMARY with error: $it")
                },
            )
        }

        syncRegistryRepository.getByTypeAndDate(
            type = SyncRegistryType.PHYSICAL_SUMMARY,
            date = selectedDate,
        ).let { syncRegistry ->
            if (syncRegistry != null) {
                Timber.i("PHYSICAL_SUMMARY for $selectedDate was already synced")
                return@let
            }

            healthConnectRepository.sync(selectedDate, SyncType.Summary.PHYSICAL).fold(
                {
                    Timber.i("Finished syncing $selectedDate PHYSICAL_SUMMARY!")

                    syncRegistryRepository.insertOrReplace(
                        SyncRegistryType.PHYSICAL_SUMMARY,
                        selectedDate
                    )
                },
                {
                    Timber.e("Failed to sync $selectedDate PHYSICAL_SUMMARY with error: $it")
                },
            )
        }

        syncRegistryRepository.getByTypeAndDate(
            type = SyncRegistryType.BODY_SUMMARY,
            date = selectedDate,
        ).let { syncRegistry ->
            if (syncRegistry != null) {
                Timber.i("BODY_SUMMARY for $selectedDate was already synced")
                return@let
            }

            healthConnectRepository.sync(selectedDate, SyncType.Summary.BODY).fold(
                {
                    Timber.i("Finished syncing $selectedDate BODY_SUMMARY!")

                    syncRegistryRepository.insertOrReplace(
                        SyncRegistryType.BODY_SUMMARY,
                        selectedDate
                    )
                },
                {
                    Timber.e("Failed to sync $selectedDate BODY_SUMMARY with error: $it")
                },
            )
        }

        syncRegistryRepository.getByTypeAndDate(
            type = SyncRegistryType.ACTIVITY_EVENT,
            date = selectedDate,
        ).let { syncRegistry ->
            if (syncRegistry != null) {
                Timber.i("ACTIVITY_EVENT for $selectedDate was already synced")
                return@let
            }

            healthConnectRepository.syncEvents(selectedDate, SyncType.Event.ACTIVITY).fold(
                {
                    Timber.i("Finished syncing $selectedDate ACTIVITY_EVENT!")

                    syncRegistryRepository.insertOrReplace(
                        SyncRegistryType.ACTIVITY_EVENT,
                        selectedDate
                    )
                },
                {
                    Timber.e("Failed to sync $selectedDate ACTIVITY_EVENT with error: $it")
                },
            )
        }
    }

    companion object {
        private const val TAG = "RookForegroundSync"
        private const val UNIQUE_NAME = "io.tryrook.recipes.RookForegroundSync"

        private val ONE_DAY = Period.ofDays(1)
        private val TWENTY_NINE_DAYS = Period.ofDays(29)
        private val THIRTY_DAYS = Period.ofDays(30)

        @RequiresPermission(
            allOf = [
                "android.permission.ACTIVITY_RECOGNITION",
                "android.permission.POST_NOTIFICATIONS",
                "android.permission.FOREGROUND_SERVICE",
                "android.permission.FOREGROUND_SERVICE_HEALTH",
            ],
        )
        fun syncNow(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build()

            val oneTimeWorkRequest = OneTimeWorkRequestBuilder<RookForegroundSync>()
                .addTag(TAG)
                .setConstraints(constraints)
                .setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.LINEAR,
                    backoffDelay = WorkRequest.MIN_BACKOFF_MILLIS,
                    timeUnit = TimeUnit.MILLISECONDS,
                )
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                /* uniqueWorkName = */ UNIQUE_NAME,
                /* existingWorkPolicy = */ ExistingWorkPolicy.KEEP,
                /* work = */ oneTimeWorkRequest,
            )
        }
    }
}

fun buildForegroundInfo(context: Context): ForegroundInfo {
    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setOnlyAlertOnce(true)
        .setOngoing(true)
        .setContentTitle("Health Connect Sync")
        .setContentText("Syncing Health Connect Data...")
        .setTicker("Syncing Health Connect Data...")
        .build()

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        ForegroundInfo(
            /* notificationId = */ NOTIFICATION_ID,
            /* notification = */ notification,
            /* foregroundServiceType = */ ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH,
        )
    } else {
        ForegroundInfo(
            /* notificationId = */ NOTIFICATION_ID,
            /* notification = */ notification,
        )
    }
}

private const val CHANNEL_ID = "io.tryrook.recipes.CHANNEL_ID"
private const val NOTIFICATION_ID = 74502674


enum class SyncRegistryType {
    SLEEP_SUMMARY,
    PHYSICAL_SUMMARY,
    BODY_SUMMARY,
    ACTIVITY_EVENT,
}

data class SyncRegistry(
    val type: SyncRegistryType,
    val date: LocalDate,
)

interface SyncRegistryRepository {
    suspend fun insertOrReplace(type: SyncRegistryType, date: LocalDate)

    suspend fun getByTypeAndDate(type: SyncRegistryType, date: LocalDate): SyncRegistry?

    suspend fun clearOlderThan(date: LocalDate)
}

// Skipped error handling for simplicity
class SyncRegistryRepositoryImpl : SyncRegistryRepository {
    private val syncRegistry = mutableListOf<SyncRegistry>()

    override suspend fun insertOrReplace(
        type: SyncRegistryType,
        date: LocalDate
    ) {
        syncRegistry.add(SyncRegistry(type, date))
    }

    override suspend fun getByTypeAndDate(
        type: SyncRegistryType,
        date: LocalDate
    ): SyncRegistry? {
        return syncRegistry.find { it.type == type && it.date == date }
    }

    override suspend fun clearOlderThan(date: LocalDate) {
        syncRegistry.removeAll { it.date.isBefore(date) }
    }
}

fun checkRookForegroundSyncAndroidPermissions(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val granted = ContextCompat.checkSelfPermission(
            /* context = */ context,
            /* permission = */ Manifest.permission.ACTIVITY_RECOGNITION,
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            return false
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val granted = ContextCompat.checkSelfPermission(
            /* context = */ context,
            /* permission = */ Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            return false
        }
    }

    // Foreground Service and Foreground Service Health are granted by default 

    return true
}
