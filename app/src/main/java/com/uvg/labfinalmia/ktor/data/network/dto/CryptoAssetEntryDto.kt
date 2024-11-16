package com.uvg.labfinalmia.ktor.data.network.dto

import com.uvg.labfinalmia.util.CryptoAsset
import kotlinx.serialization.Serializable

@Serializable
data class CryptoAssetEntryDto(
    val data: CryptoAssetDto,
    val timestamp: Long
)

fun CryptoAssetEntryDto.mapToCryptoAssetModel(): CryptoAsset {
    return CryptoAsset(
        id = data.id,
        name = data.name,
        symbol = data.symbol,
        priceUsd = data.priceUsd,
        changePercent24Hr = data.changePercent24Hr,
        supply = data.supply,
        maxSupply = data.maxSupply,
        marketCapUsd = data.marketCapUsd
    )
}