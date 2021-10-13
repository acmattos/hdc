package br.com.acmattos.hdc.common.tool.exception

import br.com.acmattos.hdc.common.tool.loggable.Loggable
import com.mongodb.MongoException

/**
 * @author ACMattos
 * @since 29/06/2020.
 */
object ExceptionCatcher: Loggable() {
    fun <T> catch(message: String, vararg values: String, block: () -> T): T =
        try {
            logger.trace(message, *values)
            block()
        } catch (ex: Exception) {
            val msg = when(ex) {
                is MongoException ->
                    "${ex.code} - ${ex.errorLabels.joinToString { it }} - ${ex.message}"
                else -> ex.cause?.message
                    ?: ex.message
                    ?: "No message was provided for this exception!"
            }
            logger.error(message, ex, msg)
            throw InternalServerErrorException(msg, ex)// TODO handle this exception in JavalinServer
        }
}

