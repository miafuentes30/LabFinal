package com.uvg.labfinalmia.room.data.localdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uvg.labfinalmia.util.CryptoAsset

@Entity(tableName = "crypto_assets")
data class CryptoAssetEntity(
    @PrimaryKey val id: String,
    val name: String,
    val symbol: String,
    val priceUsd: String,
    val changePercent24Hr: String,
    val supply: String,
    val maxSupply: String? = null,
    val marketCapUsd: String,
    val lastModified: String? = null
)

fun CryptoAssetEntity.toCryptoAsset(): CryptoAsset {
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

fun CryptoAsset.toEntity(): CryptoAssetEntity {
    return CryptoAssetEntity(
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