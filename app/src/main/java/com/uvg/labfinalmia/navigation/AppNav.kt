package com.uvg.labfinalmia.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import com.uvg.labfinalmia.cryptoassetlist.CryptoAssetListDestination
import com.uvg.labfinalmia.cryptoassetlist.cryptoAssetListScreen
import com.uvg.labfinalmia.cryptoassetprofile.cryptoAssetProfileScreen
import com.uvg.labfinalmia.cryptoassetprofile.navigateToCryptoAssetProfileScreen

@Composable
fun AppNav(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
){
    NavHost(
        navController = navController,
        startDestination = CryptoAssetListDestination,
        modifier = modifier
    ){
        cryptoAssetListScreen(
            onCryptoAssetClick = navController::navigateToCryptoAssetProfileScreen,
        )
        cryptoAssetProfileScreen(
            onBack = navController::navigateUp
        )
    }
}