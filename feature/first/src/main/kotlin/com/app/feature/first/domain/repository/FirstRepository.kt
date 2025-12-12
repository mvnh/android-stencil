package com.app.feature.first.domain.repository

import com.app.feature.first.domain.model.FeatureName
        
interface FirstRepository {
    // Define repository methods here
    
    suspend fun getFeatureName(): FeatureName
}