package br.com.acmattos.hdc.common.tool.server.javalin

import br.com.acmattos.hdc.common.tool.server.javalin.MessageTrackerIdEnum.MTI

/**
 * @author ACMattos
 * @since 30/10/2021.
 */
object MessageTrackerCodeBuilder {
    fun build() = MTI
}

enum class MessageTrackerIdEnum(
    val messageTrackerId: MessageTrackerId
): MessageTracker {
    MTI(MessageTrackerId("01FK6RXZ7BKTN1BYZW6BRHFZFJ"));
    override fun messageTrackerId() = this.messageTrackerId
}