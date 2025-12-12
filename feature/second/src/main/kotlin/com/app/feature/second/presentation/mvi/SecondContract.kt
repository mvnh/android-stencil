package com.app.feature.second.presentation.mvi
        
import androidx.compose.runtime.Immutable

object SecondContract {
        
    @Immutable
    data class State(
        // Define your state properties here
        val featureName: String = ""
    )
            
    sealed interface Intent {
        // Define your intents here
        data object GetFeatureName : Intent
    }
            
    sealed interface Effect {
        // Define your effects here
    }
}