package io.tryrook.rooknative.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.tryrook.rooknative.core.data.preferences.DefaultAppPreferences
import io.tryrook.rooknative.core.data.repository.DefaultAuthRepository
import io.tryrook.rooknative.core.domain.launcher.Launcher
import io.tryrook.rooknative.core.domain.preferences.AppPreferences
import io.tryrook.rooknative.core.domain.repository.AuthRepository
import io.tryrook.rooknative.core.framework.launcher.DefaultLauncher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SingletonModule {
    @Provides
    @Singleton
    fun provideUrlLauncher(@ApplicationContext context: Context): Launcher {
        return DefaultLauncher(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(@ApplicationContext context: Context): AuthRepository {
        return DefaultAuthRepository(context)
    }

    @Provides
    @Singleton
    fun provideAppPreferences(@ApplicationContext context: Context): AppPreferences {
        return DefaultAppPreferences(context)
    }
}
