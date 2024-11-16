package com.uvg.labfinalmia.ktor.domain.network.util

/**
 * Puedes copiar y pegar esta clase.
 */

/**
 * A la clase "Error" que estamos importando le cambié de nombre EN ESTE ARCHIVO,
 * así pueden hacer referencia a ella usando "UtilError". Esto lo hice ya que
 * más abajo, definimos una data class Error y puede llegar a ser confuso tener
 * 2 clases que se llamen Error. Si la clase util.Error se llamara diferente,
 * esto no sería necesario.
 */
import com.uvg.labfinalmia.ktor.domain.network.util.Error as UtilError



/**
 * Representa el resultado de una operación que puede ser exitosa o fallar.
 * @param D El tipo de dato en caso de éxito.
 * @param E El tipo de error en caso de fallo, debe ser una subclase de DomainError.
 */
sealed interface Result<out D, out E : UtilError> {
    /**
     * Representa un resultado exitoso.
     * @property data Los datos resultantes de la operación exitosa.
     */
    data class Success<out D>(val data: D) : Result<D, Nothing>

    /**
     * Representa un resultado fallido.
     * @property error El error que causó el fallo.
     */
    data class Error<out E : UtilError>(val error: E) : Result<Nothing, E>
}

/**
 * Transforma los datos de un Result exitoso.
 * @param map La función de transformación a aplicar a los datos.
 * @return Un nuevo Result con los datos transformados o el error original.
 */
inline fun <T, E : UtilError, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

/**
 * Ejecuta una acción si el Result es exitoso.
 * @param action La acción a ejecutar con los datos en caso de éxito.
 * @return El mismo Result, permitiendo encadenar operaciones.
 */
inline fun <T, E : UtilError> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

/**
 * Ejecuta una acción si el Result es un error.
 * @param action La acción a ejecutar con el error en caso de fallo.
 * @return El mismo Result, permitiendo encadenar operaciones.
 */
inline fun <T, E : UtilError> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            this
        }
        is Result.Success -> this
    }
}