package com.app.feature.example.data.di

import com.app.feature.example.data.repository.ExampleRepositoryImpl
import com.app.feature.example.domain.repository.ExampleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ExampleDataModule {

    @Binds
    @Singleton
    abstract fun bindExampleRepository(
        impl: ExampleRepositoryImpl
    ): ExampleRepository

    companion object {
        // Provides methods can be added here if needed in the future
    }
}