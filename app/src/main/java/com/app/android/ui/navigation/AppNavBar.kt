package com.app.android.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.navigation.NavigationDefinition

@Composable
fun AppNavBar(
    navController: NavHostController,
    definitions: List<NavigationDefinition>
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val currentScreen = remember(currentDestination, definitions) {
        definitions.find { definition ->
            currentDestination?.hierarchy?.any { destination ->
                destination.hasRoute(definition.entry)
            } == true
        }
    }
    val isBottomBarVisible = currentScreen != null

    if (!isBottomBarVisible) return

    NavigationBar {
        definitions.forEach { definition ->
            val (entryInstance, navBarItemParams) = remember(definition) {
                val entry = definition.entry
                val instance = entry.objectInstance
                val params = definition.navBarItemParams
                instance to params
            }
            if (entryInstance == null || navBarItemParams == null) return@forEach

            val isSelected = definition == currentScreen

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (isSelected) return@NavigationBarItem

                    navController.navigate(entryInstance) {
                        anim {
                            enter = android.R.anim.slide_in_left
                            exit = android.R.anim.slide_out_right
                            popEnter = android.R.anim.slide_in_left
                            popExit = android.R.anim.slide_out_right
                        }
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = navBarItemParams.iconResId),
                        contentDescription = stringResource(id = navBarItemParams.labelResId)
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = navBarItemParams.labelResId)
                    )
                }
            )
        }
    }
}