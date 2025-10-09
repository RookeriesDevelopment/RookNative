package io.tryrook.rooknative.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.tryrook.rooknative.core.framework.health.RookHealthConnectRepository
import io.tryrook.rooknative.core.framework.health.RookSamsungHealthRepository
import io.tryrook.rooknative.core.framework.health.RookStepsRepository
import io.tryrook.rooknative.feature.summaries.domain.usecase.GetAndroidSummaryUseCase
import io.tryrook.rooknative.feature.summaries.domain.usecase.GetHealthConnectSummaryUseCase
import io.tryrook.rooknative.feature.summaries.domain.usecase.GetSamsungHealthSummaryUseCase

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetHealthConnectSummaryUseCase(
        healthConnectRepository: RookHealthConnectRepository,
    ): GetHealthConnectSummaryUseCase {
        return GetHealthConnectSummaryUseCase(healthConnectRepository)
    }

    @Provides
    fun provideGetSamsungHealthSummaryUseCase(
        samsungHealthRepository: RookSamsungHealthRepository,
    ): GetSamsungHealthSummaryUseCase {
        return GetSamsungHealthSummaryUseCase(samsungHealthRepository)
    }

    @Provides
    fun provideGetAndroidSummaryUseCase(
        stepsRepository: RookStepsRepository,
    ): GetAndroidSummaryUseCase {
        return GetAndroidSummaryUseCase(stepsRepository)
    }
}
