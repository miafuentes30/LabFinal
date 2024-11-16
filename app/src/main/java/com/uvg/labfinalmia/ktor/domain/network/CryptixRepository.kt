package com.uvg.labfinalmia.ktor.domain.network

import com.uvg.labfinalmia.ktor.domain.network.util.DataError
import com.uvg.labfinalmia.util.CryptoAsset
import com.uvg.labfinalmia.ktor.domain.network.util.Result

interface CryptixRepository {
    suspend fun getAllCryptoAssets(): Result<List<CryptoAsset>, DataError>
    suspend fun getCryptoAssetById(id: String): Result<CryptoAsset, DataError>
    suspend fun saveCryptoAssets(): Result<Boolean, DataError>
}