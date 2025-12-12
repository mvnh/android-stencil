package com.app.android.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.app.feature.first.presentation.navigation.FirstGraph
import com.app.navigation.Navigable
import com.app.navigation.NavigableGraph
import com.app.navigation.NavigationDefinition
import com.app.navigation.PreviousScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    entries: Set<NavigationDefinition>,
    startDestination: NavigableGraph = FirstGraph
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        entries.forEach { entry ->
            entry.register { navigable, function ->
                navController.navigateTo(navigable, navOptions {
                    anim {
                        enter = android.R.anim.slide_in_left
                        exit = android.R.anim.slide_out_right
                        popEnter = android.R.anim.slide_in_left
                        popExit = android.R.anim.slide_out_right
                    }
                    launchSingleTop = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    restoreState = true
                    function()
                })
            }(this)
        }
    }
}

private fun NavHostController.navigateTo(
    navigable: Navigable,
    function: NavOptions?,
) {
    when (navigable) {
        is PreviousScreen -> {
            val isBackStackEmpty = previousBackStackEntry == null
            if (!isBackStackEmpty) {
                popBackStack()
            }
        }
        else -> navigate(navigable, function)
    }
}