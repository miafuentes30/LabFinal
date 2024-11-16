package com.uvg.labfinalmia.ktor.data.network

import com.uvg.labfinalmia.ktor.data.network.dto.CryptoAssetDto
import com.uvg.labfinalmia.ktor.data.network.dto.CryptoAssetEntryDto
import com.uvg.labfinalmia.ktor.data.network.dto.CryptoAssetListDto
import com.uvg.labfinalmia.ktor.data.network.util.safeCall
import com.uvg.labfinalmia.ktor.domain.network.CryptixApi
import com.uvg.labfinalmia.ktor.domain.network.util.NetworkError
import com.uvg.labfinalmia.ktor.domain.network.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class KtorCryptixApi(
    private val httpClient: HttpClient
): CryptixApi {
    override suspend fun getAllCryptoAssets(): Result<CryptoAssetListDto, NetworkError> {
        return safeCall<CryptoAssetListDto> {
            httpClient.get(
                "http://api.coincap.io/v2/assets/"
            )
        }
    }

    override suspend fun getCryptoAsset(id: String): Result<CryptoAssetEntryDto, NetworkError> {
        println("getting crypto asset by id in cryptix api")
        println("http://api.coincap.io/v2/assets/${id}")
        return safeCall<CryptoAssetEntryDto> {
            httpClient.get(
                "http://api.coincap.io/v2/assets/${id}"
            )
        }
    }
}