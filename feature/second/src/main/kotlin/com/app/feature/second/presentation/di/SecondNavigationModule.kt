package com.app.feature.second.presentation.di

import com.app.feature.second.presentation.navigation.SecondNavigationDefinition
import com.app.navigation.NavigationDefinition
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object SecondNavigationModule {

    @Provides
    @IntoSet
    fun provideSecondNavDefinition(): NavigationDefinition = SecondNavigationDefinition()
}