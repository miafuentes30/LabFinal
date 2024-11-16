package com.uvg.labfinalmia.cryptoassetlist

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object CryptoAssetListDestination

fun NavGraphBuilder.cryptoAssetListScreen(
    onCryptoAssetClick: (String) -> Unit,
) {
    composable<CryptoAssetListDestination> {
        CryptoAssetListRoute(
            onCryptoAssetClick = onCryptoAssetClick
        )
    }
}