package com.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

/**
 * Represents the parameters required to define a single item in a navigation bar.
 * This class is immutable to ensure stability when used in Compose UIs.
 *
 * @property order The display order of the item in the navigation bar. Lower numbers appear first.
 * @property labelResId The string resource ID for the item's label.
 * @property iconResId The drawable resource ID for the item's icon.
 */
@Immutable
data class NavBarItemParams(
    val order: Int,
    @param:StringRes val labelResId: Int,
    @param:DrawableRes val iconResId: Int
)
