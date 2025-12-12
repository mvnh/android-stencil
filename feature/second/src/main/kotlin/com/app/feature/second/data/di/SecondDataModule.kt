package com.app.feature.second.data.di

import com.app.feature.second.data.repository.SecondRepositoryImpl
import com.app.feature.second.domain.repository.SecondRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SecondDataModule {

    @Binds
    @Singleton
    abstract fun bindSecondRepository(
        impl: SecondRepositoryImpl
    ): SecondRepository

    companion object {
        // Provides methods can be added here if needed in the future
    }
}