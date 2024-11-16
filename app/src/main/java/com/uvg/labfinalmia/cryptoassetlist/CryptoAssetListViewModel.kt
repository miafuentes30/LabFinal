package com.uvg.labfinalmia.cryptoassetlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uvg.labfinalmia.ktor.domain.network.util.map
import com.uvg.labfinalmia.ktor.domain.network.util.onError
import com.uvg.labfinalmia.ktor.domain.network.util.onSuccess
import com.uvg.labfinalmia.room.data.localdb.dao.CryptoAssetDao
import com.uvg.labfinalmia.room.data.localdb.di.Dependencies
import com.uvg.labfinalmia.room.data.localdb.entity.toCryptoAsset
import com.uvg.labfinalmia.room.data.localdb.entity.toEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.labfinalmia.ktor.data.network.KtorCryptixApi
import com.uvg.labfinalmia.ktor.di.KtorDependencies
import com.uvg.labfinalmia.ktor.domain.network.CryptixRepository
import com.uvg.labfinalmia.room.data.localdb.CryptixRepositoryImplementation
import kotlinx.coroutines.delay
import com.uvg.labfinalmia.util.CryptoAsset

data class CryptoAssetListScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val cryptoAssets: List<CryptoAsset> = emptyList()
)

class CryptoAssetListViewModel(
    private val cryptixRepository: CryptixRepository,
    private val cryptoAssetDao: CryptoAssetDao
) : ViewModel() {

    private val _state = MutableStateFlow(CryptoAssetListScreenState())
    val state = _state.asStateFlow()

    init {
        fetchCryptoAssets()
    }

    private fun fetchCryptoAssets() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            cryptixRepository
                .getAllCryptoAssets()
                .map { response -> response.map { it.toEntity() } }
                .onSuccess { entities ->
                    val cryptoAssets = entities.map { it.toCryptoAsset() }
                    _state.update { it.copy(cryptoAssets = cryptoAssets, isLoading = false) }
                    println("CryptoAssets fetched successfully from API.")
                }
                .onError { error ->
                    handleApiError(error)
                }
        }
    }

    private suspend fun handleApiError(error: Throwable) {
        val localCryptoAssets = cryptoAssetDao.getAllCryptoAssets()
        if (localCryptoAssets.isEmpty()) {
            _state.update { it.copy(error = error.message, isLoading = false) }
        } else {
            val cryptoAssets = localCryptoAssets.map { it.toCryptoAsset() }
            _state.update { it.copy(cryptoAssets = cryptoAssets, isLoading = false) }
            println("CryptoAssets loaded from local database.")
        }
        delay(4000) // Optional delay for user experience.
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                val database = Dependencies.provideDatabase(application)
                CryptoAssetListViewModel(
                    cryptixRepository = CryptixRepositoryImplementation(
                        cryptoAssetDao = database.cryptoAssetDao(),
                        cryptixApi = KtorCryptixApi(KtorDependencies.provideHttpClient())
                    ),
                    cryptoAssetDao = database.cryptoAssetDao()
                )
            }
        }
    }
}
