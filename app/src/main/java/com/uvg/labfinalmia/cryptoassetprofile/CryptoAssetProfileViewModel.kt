package com.uvg.labfinalmia.cryptoassetprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uvg.labfinalmia.room.data.localdb.di.Dependencies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.uvg.labfinalmia.ktor.domain.network.util.Result
import com.uvg.labfinalmia.util.CryptoAsset
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.labfinalmia.ktor.data.network.KtorCryptixApi
import com.uvg.labfinalmia.ktor.di.KtorDependencies
import com.uvg.labfinalmia.ktor.domain.network.CryptixRepository
import com.uvg.labfinalmia.room.data.localdb.CryptixRepositoryImplementation

data class CryptoAssetProfileScreenState(
    val cryptoAsset: CryptoAsset? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class CryptoAssetProfileViewModel(
    private val cryptixRepository: CryptixRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CryptoAssetProfileScreenState())
    val state = _state.asStateFlow()

    /**
     * Fetches a crypto asset by its ID and updates the state accordingly.
     */
    fun getCryptoAsset(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = cryptixRepository.getCryptoAssetById(id)
            handleResult(result)
        }
    }

    /**
     * Handles the result from the repository and updates the state.
     */
    private fun handleResult(result: Result<CryptoAsset>) {
        when (result) {
            is Result.Success -> {
                _state.update { it.copy(cryptoAsset = result.data, isLoading = false) }
            }
            is Result.Error -> {
                _state.update { it.copy(error = result.error.message ?: "Unknown error", isLoading = false) }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                val api = KtorCryptixApi(KtorDependencies.provideHttpClient())
                val db = Dependencies.provideDatabase(application)
                CryptoAssetProfileViewModel(
                    cryptixRepository = CryptixRepositoryImplementation(
                        cryptoAssetDao = db.cryptoAssetDao(),
                        cryptixApi = api
                    )
                )
            }
        }
    }
}
