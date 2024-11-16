package com.uvg.labfinalmia.ktor.data.network.dto

import com.uvg.labfinalmia.util.CryptoAsset
import kotlinx.serialization.Serializable

@Serializable
data class CryptoAssetDto(
    val id: String,
    val name: String,
    val symbol: String,
    val priceUsd: String,
    val changePercent24Hr: String,
    val supply: String,
    val maxSupply: String? = null,
    val marketCapUsd: String,
    val lastModified: String? = null
)



fun CryptoAssetDto.mapToCryptoAssetModel(): CryptoAsset {
    return CryptoAsset(
        id = id,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr,
        supply = supply,
        maxSupply = maxSupply,
        marketCapUsd = marketCapUsd,
        lastModified = lastModified
    )
}
