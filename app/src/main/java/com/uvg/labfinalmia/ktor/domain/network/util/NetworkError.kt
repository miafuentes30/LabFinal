package com.uvg.labfinalmia.ktor.domain.network.util

interface Error

/**
 * Clase que enlista todos los posibles errores del API que nosotros "soportaremos" en el app.
 * Aquí deberíamos enlistar los errores que sabemos que, de una forma u otra, los manejaremos
 * en el app. Por ejemplo, si hay un timeout, mostraremos un mensaje diciendo que la conexión
 * de internet puede estar baja. Si no hay internet, decimos eso al usuario... y así con todos.
 * Si no vamos a hacer un manejo de errores tan extenso, podríamos quedarnos con 2 o 3 tipos.
 *
 * Esta clase la puedes copar y pegar
 */
enum class NetworkError: Error {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    SERVER_ERROR,
    SERIALIZATION,
    UNKNOWN,
}