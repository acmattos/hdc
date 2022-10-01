package br.com.acmattos.hdc.common.context.config

import br.com.acmattos.hdc.common.tool.server.javalin.MessageTracker
import br.com.acmattos.hdc.common.tool.server.javalin.MessageTrackerId

/**
 * @author ACMattos
 * @since 29/10/2021.
 */
enum class MessageTrackerCodeEnum(
    val messageTrackerId: MessageTrackerId
): MessageTracker {
    SAVE_FAILED(MessageTrackerId("01FK69JRHFKTN1BYZW6BRHFZFJ")),
    UPDATE_FAILED(MessageTrackerId("01FVT3QG3QGRN5Q8KG39FK090X")),
    FIND_ONE_BY_FILTER_FAILED(MessageTrackerId("01FVT3QG3Q2XQ2K4549SYH51FN")),
    FIND_ALL_BY_FILTER_FAILED(MessageTrackerId("01FVT3QG3Q52MV85FW4ZTYTE6F")),
    FIND_ALL_FAILED(MessageTrackerId("01FN4KS8QG9FE3F55670TQCPE3")),
    FIND_ALL_BY_PAGE(MessageTrackerId("01FVT3QG3R27PHS1MQ52NS823F")),
    CAUGHT_EXCEPTION(MessageTrackerId("01FK69M8XQKTN1BYZW6BRHFZFJ"));

    override fun messageTrackerId() = this.messageTrackerId
}
