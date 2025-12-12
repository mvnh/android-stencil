package com.app.feature.first.domain.usecase
        
import com.app.feature.first.domain.repository.FirstRepository
import javax.inject.Inject
        
class GetFeatureNameUseCase @Inject constructor(
    private val repository: FirstRepository
) {
    suspend operator fun invoke(): Result<String> {
        return try {
            val featureName = repository.getFeatureName()
            Result.success(featureName.value)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}