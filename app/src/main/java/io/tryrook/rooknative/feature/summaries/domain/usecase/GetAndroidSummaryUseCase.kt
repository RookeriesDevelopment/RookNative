package io.tryrook.rooknative.feature.summaries.domain.usecase

import arrow.core.getOrElse
import io.tryrook.rooknative.core.framework.health.RookStepsRepository
import io.tryrook.rooknative.feature.summaries.domain.model.Summary
import kotlinx.coroutines.coroutineScope

class GetAndroidSummaryUseCase(
    private val stepsRepository: RookStepsRepository,
) {
    suspend operator fun invoke(): Summary? = coroutineScope {
        val isConnected = stepsRepository.isBackgroundAndroidStepsActive()

        if (!isConnected) {
            // Android steps are not connected (or user has disconnected it),
            // even if permissions are granted we should not access the data.
            return@coroutineScope null
        }

        val stepsCount = stepsRepository.syncTodayAndroidStepsCount().getOrElse { 0L }

        Summary(
            stepsCount = stepsCount.toInt(),
            caloriesCount = 0,
            sleepDurationInHours = 0.0,
        )
    }
}
