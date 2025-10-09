package io.tryrook.rooknative.feature.summaries.domain.usecase

import arrow.core.getOrElse
import io.tryrook.rooknative.core.domain.extension.to2Decimals
import io.tryrook.rooknative.core.framework.health.RookSamsungHealthRepository
import io.tryrook.rooknative.feature.summaries.domain.model.Summary
import io.tryrook.sdk.samsung.domain.model.SHCalories
import io.tryrook.sdk.samsung.domain.model.SHSleepSummary
import io.tryrook.sdk.samsung.domain.model.SHSyncStatusWithData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.LocalDate
import kotlin.math.roundToInt

class GetSamsungHealthSummaryUseCase(
    private val samsungHealthRepository: RookSamsungHealthRepository,
) {
    suspend operator fun invoke(): Summary? = coroutineScope {
        val isConnected = samsungHealthRepository.isScheduled().getOrElse { false }

        if (!isConnected) {
            // Samsung Health is not connected (or user has disconnected it),
            // even if permissions are granted we should not access the data.
            return@coroutineScope null
        }

        val today = LocalDate.now()

        val stepsCount = async {
            val syncStatus = samsungHealthRepository.getTodayStepsCount().getOrElse {
                SHSyncStatusWithData.RecordsNotFound
            }

            when (syncStatus) {
                is SHSyncStatusWithData.Synced<Int> -> syncStatus.data
                SHSyncStatusWithData.RecordsNotFound -> 0
            }
        }

        val caloriesCount = async {
            val syncStatus = samsungHealthRepository.getTodayCaloriesCount().getOrElse {
                SHSyncStatusWithData.RecordsNotFound
            }

            when (syncStatus) {
                is SHSyncStatusWithData.Synced<SHCalories> -> {
                    // Get total calories by adding active and basal calories
                    (syncStatus.data.active + syncStatus.data.basal).roundToInt()
                }

                SHSyncStatusWithData.RecordsNotFound -> 0
            }
        }

        val sleepDurationInHours = async {
            val syncStatus = samsungHealthRepository.getSleepSummary(date = today).getOrElse {
                SHSyncStatusWithData.RecordsNotFound
            }

            when (syncStatus) {
                is SHSyncStatusWithData.Synced<List<SHSleepSummary>> -> {
                    // Get total sleep duration by adding all sleep durations
                    val totalDurationInHours = syncStatus.data.fold(0.0) { acc, summary ->
                        val durationInSeconds = summary.sleepDurationSeconds?.toDouble() ?: 0.0
                        val durationInHours = durationInSeconds / SECONDS_IN_AN_HOUR

                        acc + durationInHours
                    }

                    totalDurationInHours.to2Decimals()
                }

                SHSyncStatusWithData.RecordsNotFound -> 0.0
            }
        }

        Summary(
            stepsCount = stepsCount.await(),
            caloriesCount = caloriesCount.await(),
            sleepDurationInHours = sleepDurationInHours.await(),
        )
    }
}

private const val SECONDS_IN_AN_HOUR = 3600.0
