package com.uvg.labfinalmia.room.data.localdb

import com.uvg.labfinalmia.ktor.data.network.dto.mapToCryptoAssetModel
import com.uvg.labfinalmia.ktor.domain.network.CryptixApi
import com.uvg.labfinalmia.ktor.domain.network.CryptixRepository
import com.uvg.labfinalmia.ktor.domain.network.util.DataError
import com.uvg.labfinalmia.ktor.domain.network.util.NetworkError
import com.uvg.labfinalmia.room.data.localdb.dao.CryptoAssetDao
import com.uvg.labfinalmia.room.data.localdb.entity.toEntity
import com.uvg.labfinalmia.util.CryptoAsset
import com.uvg.labfinalmia.ktor.domain.network.util.Result
import com.uvg.labfinalmia.room.data.localdb.entity.toCryptoAsset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CryptixRepositoryImplementation(
    private val cryptoAssetDao: CryptoAssetDao,
    private val cryptixApi: CryptixApi
) : CryptixRepository {


    override suspend fun getAllCryptoAssets(): Result<List<CryptoAsset>, DataError> {
        return when (val result = cryptixApi.getAllCryptoAssets()) {
            is Result.Success -> {
                val remoteCryptoAssets = result.data.data.map { it.mapToCryptoAssetModel() }
                Result.Success(remoteCryptoAssets)
            }
            is Result.Error -> {
                handleLocalFallback(result.error)
            }
        }
    }


    override suspend fun saveCryptoAssets(): Result<Boolean, DataError> {
        val currentDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        return when (val remoteCryptoAssets = getAllCryptoAssets()) {
            is Result.Success -> {
                val entities = remoteCryptoAssets.data.map {
                    it.toEntity().copy(lastModified = currentDate)
                }
                cryptoAssetDao.insertCryptoAssets(entities)
                Result.Success(true)
            }
            is Result.Error -> {
                Result.Error(DataError.GENERIC_ERROR)
            }
        }
    }

    override suspend fun getCryptoAssetById(id: String): Result<CryptoAsset, DataError> {
        return when (val remoteCryptoAsset = cryptixApi.getCryptoAsset(id)) {
            is Result.Success -> {
                Result.Success(remoteCryptoAsset.data.mapToCryptoAssetModel())
            }
            is Result.Error -> {
                val localCryptoAsset = cryptoAssetDao.getCryptoAssetById(id)
                if (localCryptoAsset != null) {
                    Result.Success(localCryptoAsset.toCryptoAsset())
                } else {
                    Result.Error(DataError.GENERIC_ERROR)
                }
            }
        }
    }

    private suspend fun handleLocalFallback(networkError: NetworkError): Result<List<CryptoAsset>, DataError> {
        val localCryptoAssets = cryptoAssetDao.getAllCryptoAssets()
        return if (localCryptoAssets.isEmpty()) {
            when (networkError) {
                NetworkError.NO_INTERNET -> Result.Error(DataError.NO_INTERNET)
                else -> Result.Error(DataError.GENERIC_ERROR)
            }
        } else {
            Result.Success(localCryptoAssets.map { it.toCryptoAsset() })
        }
    }
}
