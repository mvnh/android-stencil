package com.app.feature.second.data.repository
        
import com.app.feature.second.domain.repository.SecondRepository
import com.app.feature.second.domain.model.FeatureName
import javax.inject.Inject
        
class SecondRepositoryImpl @Inject constructor() : SecondRepository {
    // Implement repository methods here
    
    override suspend fun getFeatureName(): FeatureName {
        return FeatureName(value = "Second")
    }
}