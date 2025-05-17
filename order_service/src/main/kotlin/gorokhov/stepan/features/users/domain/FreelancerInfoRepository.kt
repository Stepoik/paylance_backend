package gorokhov.stepan.features.users.domain

import gorokhov.stepan.features.users.domain.models.Freelancer
import gorokhov.stepan.features.users.domain.models.FreelancerInfo

interface FreelancerInfoRepository {
    suspend fun getFreelancerInfo(id: String): FreelancerInfo?

    suspend fun createFreelancerInfo(freelancerInfo: FreelancerInfo): FreelancerInfo

    suspend fun updateFreelancerInfo(freelancerInfo: FreelancerInfo): FreelancerInfo
}