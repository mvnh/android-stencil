package com.app.feature.example.presentation.di

import com.app.feature.example.presentation.navigation.ExampleNavigationDefinition
import com.app.navigation.NavigationDefinition
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object ExampleNavigationModule {

    @Provides
    @IntoSet
    fun provideExampleNavDefinition(): NavigationDefinition = ExampleNavigationDefinition()
}