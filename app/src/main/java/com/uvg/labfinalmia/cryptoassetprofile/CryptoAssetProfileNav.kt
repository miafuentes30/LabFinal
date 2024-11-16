package com.uvg.labfinalmia.cryptoassetprofile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.serialization.Serializable

@Serializable
data class CryptoAssetProfileDestination(val id: String)

fun NavController.navigateToCryptoAssetProfileScreen(
    id: String,
    navOptions: NavOptions? = null
) {
    val route = "cryptoAssetProfileScreen/$id"
    this.navigate(route, navOptions)
}

fun NavGraphBuilder.cryptoAssetProfileScreen(
    onBack: () -> Unit,
) {
    composable(
        route = "cryptoAssetProfileScreen/{id}",
        arguments = listOf(navArgument("id") { type = androidx.navigation.NavType.StringType })
    ) { backStackEntry ->
        val cryptoAssetId = backStackEntry.arguments?.getString("id") ?: return@composable
        CryptoAssetProfileRoute(
            onBack = onBack,
            cryptoAssetId = cryptoAssetId
        )
    }
}

}