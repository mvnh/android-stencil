package com.app.core.di

// In a suitable module like :core or :app, inside a di package
// e.g., /core/src/main/java/com/app/core/di/DispatchersModule.kt
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

/**
 * Hilt module that provides CoroutineDispatchers.
 *
 * This module is responsible for injecting dispatchers, which helps in making classes
 * that use coroutines more testable. Instead of hardcoding `Dispatchers.IO` or `Dispatchers.Main`,
 * dependencies can now inject a `CoroutineDispatcher` qualified with `@IoDispatcher` or
 * `@MainDispatcher`. This allows for replacing the real dispatchers with a `TestDispatcher`
 * in unit tests.
 *
 * @see IoDispatcher
 * @see MainDispatcher
 */
@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher
