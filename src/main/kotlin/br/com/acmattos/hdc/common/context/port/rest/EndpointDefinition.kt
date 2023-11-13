package br.com.acmattos.hdc.common.context.port.rest

private const val MESSAGE_FORMAT = "Route Configured: -> %s %s <-"
private const val GET    = "   GET"
private const val DELETE = "DELETE"
private const val POST   = "  POST"
private const val PUT    = "   PUT"

/**
 * @author ACMattos
 * @since 26/06/2019.
 */
interface EndpointDefinition {
    /**
     * Define the routes of your endpoint here.
     */
    fun routes()

    /**
     * Get the 'Route Configured' message (POST method).
     */
    fun routePostMessage(path: String) = MESSAGE_FORMAT.format(POST, path)

    /**
     * Get the 'Route Configured' message (GET method).
     */
    fun routeGetMessage(path: String) = MESSAGE_FORMAT.format(GET, path)

    /**
     * Get the 'Route Configured' message (PUT method).
     */
    fun routePutMessage(path: String) = MESSAGE_FORMAT.format(PUT, path)

    /**
     * Get the 'Route Configured' message (DELETE method).
     */
    fun routeDeleteMessage(path: String) = MESSAGE_FORMAT.format(DELETE, path)
}
