package com.app.feature.first.presentation.di

import com.app.feature.first.presentation.navigation.FirstNavigationDefinition
import com.app.navigation.NavigationDefinition
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object FirstNavigationModule {

    @Provides
    @IntoSet
    fun provideFirstNavDefinition(): NavigationDefinition = FirstNavigationDefinition()
}