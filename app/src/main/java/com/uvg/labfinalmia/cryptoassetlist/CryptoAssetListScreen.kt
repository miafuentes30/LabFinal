package com.uvg.labfinalmia.cryptoassetlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uvg.labfinalmia.util.CryptoAsset

@Composable
fun CryptoAssetListRoute(
    onCryptoAssetClick: (String) -> Unit,
    viewModel: CryptoAssetListViewModel = viewModel(factory = CryptoAssetListViewModel.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CryptoAssetListScreen(
        state = state,
        onCryptoAssetClick = onCryptoAssetClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoAssetListScreen(
    state: CryptoAssetListScreenState,
    onCryptoAssetClick: (String) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "CryptoAssets") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        when {
            state.isLoading -> LoadingView()
            state.error != null -> ErrorView(state.error)
            state.cryptoAssets.isNotEmpty() -> CryptoAssetsList(
                cryptoAssets = state.cryptoAssets,
                onCryptoAssetClick = onCryptoAssetClick
            )
        }
    }
}

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Loading cryptoAssets...", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun ErrorView(error: String) {
    Text(
        text = "Error: $error",
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun CryptoAssetsList(
    cryptoAssets: List<CryptoAsset>,
    onCryptoAssetClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
    ) {
        items(cryptoAssets) { cryptoAsset ->
            CryptoAssetRow(cryptoAsset = cryptoAsset, onClickCrypto = onCryptoAssetClick)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun CryptoAssetRow(cryptoAsset: CryptoAsset, onClickCrypto: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp)
            .clickable { onClickCrypto(cryptoAsset.id) }
    ) {
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(
                text = cryptoAsset.name,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Symbol: ${cryptoAsset.symbol}")
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Price: ${cryptoAsset.priceUsd}")
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Change %: ${cryptoAsset.changePercent24Hr}",
                    style = TextStyle(
                        color = if (cryptoAsset.changePercent24Hr.toFloatOrNull() ?: 0f < 0) Color.Red else Color.Green
                    )
                )
            }
        }
    }
}
