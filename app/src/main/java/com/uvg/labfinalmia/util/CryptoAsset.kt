package com.uvg.labfinalmia.util

import kotlinx.serialization.Serializable
import java.util.Date


@Serializable
data class CryptoAsset(
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