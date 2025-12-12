package com.app.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable

@Immutable
data class NavBarItemParams(
    val order: Int,
    @param:StringRes val labelResId: Int,
    @param:DrawableRes val iconResId: Int
)
