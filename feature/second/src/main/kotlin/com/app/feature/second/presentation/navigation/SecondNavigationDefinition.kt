package com.app.feature.second.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.app.feature.second.R
import com.app.feature.second.presentation.screen.SecondScreen
import com.app.navigation.NavBarItemParams
import com.app.navigation.NavigationDefinition
import com.app.navigation.OnNavigateTo

class SecondNavigationDefinition : NavigationDefinition {

    override val entry = SecondGraph::class
    override val navBarItemParams = NavBarItemParams(
        labelResId = R.string.second,
        // TODO: Replace with your order value
        order = Int.MAX_VALUE,
        // TODO: Replace with your own icon
        iconResId = R.drawable.outline_android_24
    )

    override fun register(onNavigateTo: OnNavigateTo): NavGraphBuilder.() -> Unit = {
        navigation<SecondGraph>(
            startDestination = SecondRoute
        ) {
            composable<SecondRoute> {
                SecondScreen(onNavigateTo)
            }
            
            // Add more composable destinations here if needed
        }
    }
}