package io.tryrook.rooknative.feature.summaries.domain.usecase

import arrow.core.getOrElse
import com.rookmotion.rook.sdk.domain.model.DailyCalories
import com.rookmotion.rook.sdk.domain.model.HCSleepSummary
import com.rookmotion.rook.sdk.domain.model.SyncStatusWithData
import io.tryrook.rooknative.core.domain.extension.to2Decimals
import io.tryrook.rooknative.core.framework.health.RookHealthConnectRepository
import io.tryrook.rooknative.feature.summaries.domain.model.Summary
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.LocalDate
import kotlin.math.roundToInt

class GetHealthConnectSummaryUseCase(
    private val healthConnectRepository: RookHealthConnectRepository,
) {
    suspend operator fun invoke(): Summary? = coroutineScope {
        val isConnected = healthConnectRepository.isScheduled().getOrElse { false }

        if (!isConnected) {
            // Health Connect is not connected (or user has disconnected it),
            // even if permissions are granted we should not access the data.
            return@coroutineScope null
        }

        val today = LocalDate.now()

        val stepsCount = async {
            val syncStatus = healthConnectRepository.getTodayStepsCount().getOrElse {
                SyncStatusWithData.RecordsNotFound
            }

            when (syncStatus) {
                is SyncStatusWithData.Synced<Int> -> syncStatus.data
                SyncStatusWithData.RecordsNotFound -> 0
            }
        }

        val caloriesCount = async {
            val syncStatus = healthConnectRepository.getTodayCaloriesCount().getOrElse {
                SyncStatusWithData.RecordsNotFound
            }

            when (syncStatus) {
                is SyncStatusWithData.Synced<DailyCalories> -> {
                    // Get total calories by adding active and basal calories
                    (syncStatus.data.active + syncStatus.data.basal).roundToInt()
                }

                SyncStatusWithData.RecordsNotFound -> 0
            }
        }

        val sleepDurationInHours = async {
            val syncStatus = healthConnectRepository.getSleepSummary(date = today).getOrElse {
                SyncStatusWithData.RecordsNotFound
            }

            when (syncStatus) {
                is SyncStatusWithData.Synced<List<HCSleepSummary>> -> {
                    // Get total sleep duration by adding all sleep durations
                    val totalDurationInHours = syncStatus.data.fold(0.0) { acc, summary ->
                        val durationInSeconds = summary.sleepDurationSeconds?.toDouble() ?: 0.0
                        val durationInHours = durationInSeconds / SECONDS_IN_AN_HOUR

                        acc + durationInHours
                    }

                    totalDurationInHours.to2Decimals()
                }

                SyncStatusWithData.RecordsNotFound -> 0.0
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
