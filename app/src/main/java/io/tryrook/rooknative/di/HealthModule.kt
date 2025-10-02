package io.tryrook.rooknative.di

import android.content.Context
import com.rookmotion.rook.sdk.RookBackgroundSyncManager
import com.rookmotion.rook.sdk.RookConfigurationManager
import com.rookmotion.rook.sdk.RookDataSources
import com.rookmotion.rook.sdk.RookPermissionsManager
import com.rookmotion.rook.sdk.RookStepsManager
import com.rookmotion.rook.sdk.RookSyncManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.tryrook.rooknative.BuildConfig
import io.tryrook.rooknative.core.domain.repository.HealthRepository
import io.tryrook.rooknative.core.framework.health.RookHealthRepository
import io.tryrook.sdk.samsung.RookSamsung
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HealthModule {
    @Provides
    @Singleton
    fun provideRookConfigurationManager(@ApplicationContext context: Context): RookConfigurationManager {
        return RookConfigurationManager(context).apply {
            if (BuildConfig.DEBUG) {
                enableLocalLogs()
            }
        }
    }

    @Provides
    @Singleton
    fun provideRookPermissionsManager(@ApplicationContext context: Context): RookPermissionsManager {
        return RookPermissionsManager(context)
    }

    @Provides
    @Singleton
    fun provideRookSyncManager(@ApplicationContext context: Context): RookSyncManager {
        return RookSyncManager(context)
    }

    @Provides
    @Singleton
    fun provideRookBackgroundSyncManager(@ApplicationContext context: Context): RookBackgroundSyncManager {
        return RookBackgroundSyncManager(context)
    }

    @Provides
    @Singleton
    fun provideRookStepsManager(@ApplicationContext context: Context): RookStepsManager {
        return RookStepsManager(context)
    }

    @Provides
    @Singleton
    fun provideRookDataSources(@ApplicationContext context: Context): RookDataSources {
        return RookDataSources(context)
    }

    @Provides
    @Singleton
    fun provideRookSamsung(@ApplicationContext context: Context): RookSamsung {
        return RookSamsung(context).apply {
            if (BuildConfig.DEBUG) {
                enableLocalLogs()
            }
        }
    }

    @Provides
    @Singleton
    fun provideHealthRepository(
        backgroundSyncManager: RookBackgroundSyncManager,
        stepsManager: RookStepsManager,
        rookDataSources: RookDataSources,
        rookSamsung: RookSamsung,
    ): HealthRepository {
        return RookHealthRepository(
            backgroundSyncManager,
            stepsManager,
            rookDataSources,
            rookSamsung,
        )
    }
}
