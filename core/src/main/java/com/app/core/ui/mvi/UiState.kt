package com.app.core.ui.mvi

import androidx.compose.runtime.Stable

/**
 * Represents the basic state of a UI screen or component.
 * It's designed to be a common interface for MVI (Model-View-Intent) pattern implementations.
 *
 * This interface is marked as [Stable] to ensure that Compose can optimize recompositions
 * when objects of this type are used in a `State`.
 */
@Stable
interface UiState {
    val isLoading: Boolean
    val error: Throwable?
}