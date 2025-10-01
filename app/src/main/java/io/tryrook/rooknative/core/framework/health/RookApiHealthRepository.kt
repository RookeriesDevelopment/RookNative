package io.tryrook.rooknative.core.framework.health

import arrow.core.Either
import com.rookmotion.rook.sdk.RookDataSources
import com.rookmotion.rook.sdk.domain.enums.DataSourceType
import com.rookmotion.rook.sdk.domain.model.AuthorizedDataSourceV2
import com.rookmotion.rook.sdk.domain.model.AuthorizedDataSources
import com.rookmotion.rook.sdk.domain.model.DataSourceAuthorizer
import io.tryrook.rooknative.core.domain.error.HealthError
import io.tryrook.rooknative.core.domain.extension.toEither
import io.tryrook.rooknative.core.framework.error.toHealthError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RookApiHealthRepository @Inject constructor(
    private val rookDataSources: RookDataSources,
) {
    suspend fun getDataSourceAuthorizer(
        dataSource: String,
        redirectUrl: String? = null
    ): Either<HealthError, DataSourceAuthorizer> {
        return rookDataSources.getDataSourceAuthorizer(dataSource, redirectUrl)
            .toEither { it.toHealthError() }
    }

    suspend fun getAuthorizedDataSources(): Either<HealthError, AuthorizedDataSources> {
        return rookDataSources.getAuthorizedDataSources()
            .toEither { it.toHealthError() }
    }

    suspend fun getAuthorizedDataSourcesV2(): Either<HealthError, List<AuthorizedDataSourceV2>> {
        return rookDataSources.getAuthorizedDataSourcesV2()
            .toEither { it.toHealthError() }
    }

    suspend fun revokeDataSource(dataSourceType: DataSourceType): Either<HealthError, Unit> {
        return rookDataSources.revokeDataSource(dataSourceType)
            .toEither { it.toHealthError() }
    }
}
