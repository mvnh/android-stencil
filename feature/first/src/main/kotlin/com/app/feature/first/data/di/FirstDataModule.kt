package com.app.feature.first.data.di

import com.app.feature.first.data.repository.FirstRepositoryImpl
import com.app.feature.first.domain.repository.FirstRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FirstDataModule {

    @Binds
    @Singleton
    abstract fun bindFirstRepository(
        impl: FirstRepositoryImpl
    ): FirstRepository

    companion object {
        // Provides methods can be added here if needed in the future
    }
}