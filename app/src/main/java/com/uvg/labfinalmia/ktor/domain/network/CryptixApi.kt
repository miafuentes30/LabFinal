package com.uvg.labfinalmia.ktor.domain.network

import com.uvg.labfinalmia.ktor.data.network.dto.CryptoAssetDto
import com.uvg.labfinalmia.ktor.data.network.dto.CryptoAssetEntryDto
import com.uvg.labfinalmia.ktor.data.network.dto.CryptoAssetListDto
import com.uvg.labfinalmia.ktor.domain.network.util.NetworkError
import com.uvg.labfinalmia.ktor.domain.network.util.Result

interface CryptixApi {
    suspend fun getAllCryptoAssets(): Result<CryptoAssetListDto, NetworkError>
    suspend fun getCryptoAsset(id: String): Result<CryptoAssetEntryDto, NetworkError>
}