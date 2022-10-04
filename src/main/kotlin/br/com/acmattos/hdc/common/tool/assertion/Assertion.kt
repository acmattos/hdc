package br.com.acmattos.hdc.common.tool.assertion

import br.com.acmattos.hdc.common.exception.HdcGenericException
import br.com.acmattos.hdc.common.tool.assertion.AssertionLogEnum.ASSERTION
import br.com.acmattos.hdc.common.tool.assertion.AssertionLogEnum.FAILURE
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.common.tool.server.javalin.MessageTracker
import br.com.acmattos.hdc.common.tool.server.javalin.MessageTrackerId

/**
 * @author ACMattos
 * @since 12/06/2020.
 */
object Assertion: Loggable() {
    fun assert(
        message: String,
        context: String,
        messageTracker: MessageTracker,
        condition: () -> Boolean
    ) {
        logger.trace(
            "[{} <{}>] - Assertion to be evaluated...",
            "$context $ASSERTION",
            messageTracker.toString()
        )
        if(!condition()) {
            logger.info(
                "[{} <{}> {}]: -> '{}' <-",
                "$context $ASSERTION",
                messageTracker.toString(),
                FAILURE.name,
                message
            )
            throw AssertionFailedException(message, messageTracker.messageTrackerId())
        }
        logger.debug(
            "[{} <{}>] - Assertion evaluated successfully!",
            "$context $ASSERTION",
            messageTracker.toString()
        )
    }
}

/**
 * @author ACMattos
 * @since 31/10/2019.
 */
data class AssertionFailedException(
    override val message: String,
    val code: MessageTrackerId,
): HdcGenericException(message, code)
