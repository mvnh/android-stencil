package com.app.feature.first.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.app.feature.first.R
import com.app.feature.first.presentation.screen.FirstScreen
import com.app.navigation.NavBarItemParams
import com.app.navigation.NavigationDefinition
import com.app.navigation.OnNavigateTo

class FirstNavigationDefinition : NavigationDefinition {

    override val entry = FirstGraph::class
    override val navBarItemParams = NavBarItemParams(
        labelResId = R.string.first,
        // TODO: Replace with your order value
        order = Int.MAX_VALUE,
        // TODO: Replace with your own icon
        iconResId = R.drawable.outline_android_24
    )

    override fun register(onNavigateTo: OnNavigateTo): NavGraphBuilder.() -> Unit = {
        navigation<FirstGraph>(
            startDestination = FirstRoute
        ) {
            composable<FirstRoute> {
                FirstScreen(onNavigateTo)
            }
            
            // Add more composable destinations here if needed
        }
    }
}