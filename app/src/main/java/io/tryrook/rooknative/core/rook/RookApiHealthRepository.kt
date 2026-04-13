package io.tryrook.rooknative.core.rook

import arrow.core.Either
import arrow.core.left
import io.tryrook.api.sources.RookApiSources
import io.tryrook.api.sources.domain.enums.DataSourceType
import io.tryrook.api.sources.domain.model.ApiConfiguration
import io.tryrook.api.sources.domain.model.AuthorizedDataSourceV2
import io.tryrook.api.sources.domain.model.DataSourceAuthorizer
import io.tryrook.rooknative.core.domain.extension.toEither
import io.tryrook.rooknative.core.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RookApiHealthRepository @Inject constructor(
    private val rookApiSources: RookApiSources,
    private val authRepository: AuthRepository,
) {
    fun enableLocalLogs() {
        rookApiSources.enableLocalLogs()
    }

    fun initRook(configuration: ApiConfiguration) {
        rookApiSources.initRook(configuration)
    }

    suspend fun getDataSourceAuthorizer(
        dataSource: String,
        redirectUrl: String? = null
    ): Either<HealthError, DataSourceAuthorizer> {
        val userID = authRepository.getUserID() ?: return HealthError.Other("User not found").left()

        return rookApiSources.getDataSourceAuthorizer(userID, dataSource, redirectUrl)
            .toEither { it.toHealthError() }
    }

    suspend fun getAuthorizedDataSourcesV2(): Either<HealthError, List<AuthorizedDataSourceV2>> {
        val userID = authRepository.getUserID() ?: return HealthError.Other("User not found").left()

        return rookApiSources.getAuthorizedDataSourcesV2(userID)
            .toEither { it.toHealthError() }
    }

    suspend fun revokeDataSource(dataSourceType: DataSourceType): Either<HealthError, Unit> {
        val userID = authRepository.getUserID() ?: return HealthError.Other("User not found").left()

        return rookApiSources.revokeDataSource(userID, dataSourceType)
            .toEither { it.toHealthError() }
    }
}
