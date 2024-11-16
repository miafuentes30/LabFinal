package com.uvg.labfinalmia.ktor.data.network.util

import com.uvg.labfinalmia.ktor.domain.network.util.NetworkError
import com.uvg.labfilabfinalmianaljune.ktor.domain.network.util.Result
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

/**
 * Realiza una llamada de red de manera segura, manejando excepciones comunes.
 *
 * Agregamos el generico T que define el tipo de dato esperado en la respuesta.
 * @param execute Una función suspendida que realiza la llamada de red y devuelve un HttpResponse.
 * @return Un Result que contiene el dato deserializado de tipo T en caso de éxito,
 *         o un NetworkError en caso de fallo.
 *
 * Esta función maneja los siguientes errores:
 * - NO_INTERNET: Cuando no se puede resolver la dirección del servidor.
 * - SERIALIZATION: Cuando hay un problema al deserializar la respuesta.
 * - UNKNOWN: Para cualquier otra excepción no manejada específicamente.
 *
 * Puedes copiar y pegar esta funcion
 */
suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, NetworkError> {
    val response = try {
        execute()
    } catch(e: UnresolvedAddressException) {
        return Result.Error(NetworkError.NO_INTERNET)
    } catch(e: SerializationException) {
        return Result.Error(NetworkError.SERIALIZATION)
    } catch(e: Exception) {
        /**
         * Esto es necesario porque, cuando una corrutina es cancelada, deberíamos dejar que el padre
         * de la corrutina se entere de esto. El problema es que, al agarrar Exception (que esto agarra
         * CUALQUIER tipo de exception que no sean los que especificamos arriba), agarraría el Exception
         * tirado al cancelar la corrutina, y por ende, el padre no se enteraría.
         *
         * coroutineContext.ensureActive() internamente valida que si, la corrutina sigue activa,
         * entonces sigue a la siguiente línea, pero si no, tira una CancellationException.
         */
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN)
    }

    return responseToResult(response)
}