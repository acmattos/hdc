package br.com.acmattos.hdc.common.context.port.rest

import br.com.acmattos.hdc.common.tool.loggable.Loggable

private const val MESSAGE_ALL_FORMAT = "All routes loaded for: -> %s <-"
private const val MESSAGE_FORMAT = "     Route Configured: -> %s %s <-"
private const val GET    = "   GET"
private const val DELETE = "DELETE"
private const val POST   = "  POST"
private const val PUT    = "   PUT"

/**
 * @author ACMattos
 * @since 26/06/2019.
 */
abstract class EndpointDefinition {
    /**
     * Define the routes of your endpoint here.
     */
    abstract fun routes()

    /**
     * Log the 'Route Configured' message (POST method).
     */
    fun logPostRoute(path: String) {
        logger.info(MESSAGE_FORMAT.format(POST, path))
    }

    /**
     * Log the 'Route Configured' message (GET method).
     */
    fun logGetRoute(path: String) {
        logger.info(MESSAGE_FORMAT.format(GET, path))
    }

    /**
     * Log the 'Route Configured' message (PUT method).
     */
    fun logPutRoute(path: String) {
        logger.info(MESSAGE_FORMAT.format(PUT, path))
    }

    /**
     * Log the 'Route Configured' message (DELETE method).
     */
    fun logDeleteRoute(path: String) {
        logger.info(MESSAGE_FORMAT.format(DELETE, path))
    }

    /**
     * Log the 'All routes loaded' message.
     */
    fun logAllRoutesLoaded() {
        logger.info(MESSAGE_ALL_FORMAT.format(name()))
    }

    /**
     * Gets the implementation instance name.
     */
    abstract fun name(): String

    companion object: Loggable()
}
