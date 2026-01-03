package com.app.feature.example.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.app.feature.example.presentation.screen.ExampleScreen
import com.app.navigation.NavigationDefinition
import com.app.navigation.OnNavigateTo

class ExampleNavigationDefinition : NavigationDefinition {

    override val entry = ExampleGraph
    
    // Not added to navigation bar
    override val navBarItemParams = null

    override fun register(onNavigateTo: OnNavigateTo): NavGraphBuilder.() -> Unit = {
        navigation<ExampleGraph>(
            startDestination = ExampleRoute
        ) {
            composable<ExampleRoute> {
                ExampleScreen(onNavigateTo)
            }
            
            // Add more composable destinations here if needed
        }
    }
}