package com.uvg.labfinalmia.cryptoassetprofile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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


@Composable
fun CryptoAssetProfileRoute(
    cryptoAssetId: String,
    onBack: () -> Unit,
    viewModel: CryptoAssetProfileViewModel = viewModel(factory = CryptoAssetProfileViewModel.Factory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(cryptoAssetId) {
        viewModel.getCryptoAsset(cryptoAssetId)
    }

    CryptoAssetProfileScreen(state = state, onBack = onBack)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoAssetProfileScreen(
    state: CryptoAssetProfileScreenState,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "CryptoAsset Details") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        when {
            state.isLoading -> LoadingView()
            state.error != null -> ErrorView(state.error, onRetry = { onBack() })
            state.cryptoAsset != null -> CryptoAssetDetailsView(state.cryptoAsset)
        }
    }
}


@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Loading cryptoAsset details...", style = MaterialTheme.typography.bodyLarge)
        }
    }
}


@Composable
fun ErrorView(error: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Error: $error",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}


@Composable
fun CryptoAssetDetailsView(cryptoAsset: CryptoAsset) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = cryptoAsset.name,
            style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 10.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))

        CryptoAssetDetailRow("Symbol:", cryptoAsset.symbol)
        CryptoAssetDetailRow("Price:", cryptoAsset.priceUsd)
        CryptoAssetChangePercentageRow("Change %:", cryptoAsset.changePercent24Hr)
        CryptoAssetDetailRow("Supply:", cryptoAsset.supply)
        CryptoAssetDetailRow("Max Supply:", cryptoAsset.maxSupply)
        CryptoAssetDetailRow("Market Cap (USD):", cryptoAsset.marketCapUsd)
    }
}


@Composable
fun CryptoAssetDetailRow(label: String, value: String?) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Text(text = label)
        Text(
            text = value ?: "N/A",
            color = if (value.isNullOrBlank()) Color.Gray else Color.Unspecified
        )
    }
}


@Composable
fun CryptoAssetChangePercentageRow(label: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Text(text = label)
        Text(
            text = value,
            style = TextStyle(
                color = when (value.toFloatOrNull() ?: 0f) {
                    in Float.MIN_VALUE..0f -> Color.Red
                    else -> Color.Green
                }
            )
        )
    }
}
