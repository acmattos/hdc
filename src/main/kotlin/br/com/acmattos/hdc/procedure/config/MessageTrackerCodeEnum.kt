package br.com.acmattos.hdc.procedure.config

import br.com.acmattos.hdc.common.tool.server.javalin.MessageTracker
import br.com.acmattos.hdc.common.tool.server.javalin.MessageTrackerId

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
enum class MessageTrackerCodeEnum(
    val messageTrackerId: MessageTrackerId
): MessageTracker {
    PROCEDURE_ALREADY_DEFINED(MessageTrackerId("01FVQ2NP5K2XQ2K4549SYH51FN")),
    PROCEDURE_NOT_DEFINED(MessageTrackerId("01FVT3QG3P19FBHY10QF033G7Y")),
    CODE_OUT_OF_RANGE(MessageTrackerId("01FVQ2NP5M52MV85FW4ZTYTE6F")),
    DESCRIPTION_INVALID_LENGTH(MessageTrackerId("01FVQ2NP5M27PHS1MQ52NS823F"));

    override fun messageTrackerId() = this.messageTrackerId
}
