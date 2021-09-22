package br.com.acmattos.hdc.common.tool.assertion

import br.com.acmattos.hdc.common.tool.loggable.Loggable

private const val ASSERTION = "ASSERTION"
private const val FAILURE = "FAILURE"

/**
 * @author ACMattos
 * @since 12/06/2020.
 */
object Assertion: Loggable() {
    fun assert(message: String, condition: () -> Boolean) {
        logger.trace("[{}] - Assertion to be evaluated...", ASSERTION)
        if(!condition()) {
            logger.info("[{} {}]: -> '{}' <-", ASSERTION, FAILURE, message)
            throw AssertionFailedException(message)
        }
        logger.trace("[{}] - Assertion evaluated successfully!: -> '{}' <-", ASSERTION, message)
    }
}

/**
 * @author ACMattos
 * @since 31/10/2019.
 */
data class AssertionFailedException(
    override val message: String
): Exception(message)