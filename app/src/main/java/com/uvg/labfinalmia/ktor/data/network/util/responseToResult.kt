package com.uvg.labfinalmia.ktor.data.network.util

import com.uvg.labfinalmia.ktor.domain.network.util.NetworkError
import com.uvg.labfinalmia.ktor.domain.network.util.Result

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

/**
 * Esta función evalúa la respuesta del servidor y, en base al HTTP Status que retorne,
 * retornará un Result exitoso o erroneo.
 *
 * Puedes copiar y pegar esta función
 */
suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, NetworkError> {
    return when(response.status.value) {
        // Verifica que el HttpStatus sea de una llamada exitosa
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch(e: Exception) {
                Result.Error(NetworkError.SERIALIZATION)
            }
        }
        /**
         * Resto de errores. Podríamos usar menos, pero esta función sigue las mejores prácticas
         * y toma en cuenta multiples escenarios. Por fines practicos, se podrian eliminar todos
         * y dejar unicamente el else con el [NetworkError.UNKNOWN]
         */
        408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
        429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
        else -> Result.Error(NetworkError.UNKNOWN)
    }
}