package br.com.acmattos.hdc.odontogram.config

import br.com.acmattos.hdc.common.tool.server.javalin.MessageTracker
import br.com.acmattos.hdc.common.tool.server.javalin.MessageTrackerId

enum class MessageTrackerIdEnum(
    val messageTrackerId: MessageTrackerId
): MessageTracker {
    ID_OUT_OF_RANGE(MessageTrackerId("01FWKTXVD7GRN5Q8KG39FK090X")),
    SURFACE_TYPE_NOT_ALLOWED(MessageTrackerId("01FWKTXVD857RMXBZDKHVT7ZGJ")),
    NOTE_BIGGER_THAN_ALLOWED(MessageTrackerId("01FWKTXVD8KKHVWHP57PKD8N62")),
    SURFACE_TYPE_CONVERT_FAILED(MessageTrackerId("01FXTSG8Z49FE3F55670TQCPE3")),
    SURFACE_STAMP_CONVERT_FAILED(MessageTrackerId("01FXTSG8Z3Q2MJX8PESM4KVKSP"));

    override fun messageTrackerId() = this.messageTrackerId
}