package br.com.acmattos.hdc.common.context.config

import br.com.acmattos.hdc.common.tool.server.javalin.MessageTrackerCode

/**
 * @author ACMattos
 * @since 29/10/2021.
 */
enum class MessageTrackerCodeEnum(val code: MessageTrackerCode) {
    SAVE_FAILED(MessageTrackerCode("01FK69JRHFKTN1BYZW6BRHFZFJ")),
    UPDATE_FAILED(MessageTrackerCode("01FVT3QG3QGRN5Q8KG39FK090X")),
    FIND_ONE_BY_FILTER_FAILED(MessageTrackerCode("01FVT3QG3Q2XQ2K4549SYH51FN")),
    FIND_ALL_BY_FILTER_FAILED(MessageTrackerCode("01FVT3QG3Q52MV85FW4ZTYTE6F")),
    FIND_BY_FIELD_FAILED(MessageTrackerCode("01FK69KJ5JKTN1BYZW6BRHFZFJ")),
    FIND_ALL_FAILED(MessageTrackerCode("01FN4KS8QG9FE3F55670TQCPE3")),
    FIND_ALL_BY_FIELD_FAILED(MessageTrackerCode("01FK69KJ5JKTN1BYZW6BRHFZFJ")),
    FIND_ALL_BY_CRITERIA_FAILED(MessageTrackerCode("01FMMA8XX09FE3F55670TQCPE3")),
    CAUGHT_EXCEPTION(MessageTrackerCode("01FK69M8XQKTN1BYZW6BRHFZFJ")),
}
