package com.app.core.ui.theme.size

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class AppSpacingValues(
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val extraLarge: Dp,
    val textSmall: TextUnit,
    val textMedium: TextUnit,
    val textLarge: TextUnit,
    val textExtraLarge: TextUnit
)

val LocalSpacing = staticCompositionLocalOf<AppSpacingValues> {
    throw IllegalStateException()
}

@Suppress("UnusedReceiverParameter")
val MaterialTheme.spacing: AppSpacingValues
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current

internal fun getSpacingValues(): AppSpacingValues =
    AppSpacingValues(
        small = Small.dp,
        medium = Medium.dp,
        large = Large.dp,
        extraLarge = ExtraLarge.dp,
        textSmall = Small.sp,
        textMedium = Medium.sp,
        textLarge = Large.sp,
        textExtraLarge = ExtraLarge.sp
    )

private const val Small = 4
private const val Medium = 8
private const val Large = 16
private const val ExtraLarge = 32