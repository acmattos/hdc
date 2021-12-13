package br.com.acmattos.hdc.common.context.config

import br.com.acmattos.hdc.common.tool.server.javalin.ErrorTrackerCode

/**
 * @author ACMattos
 * @since 29/10/2021.
 */
enum class ErrorTrackerCodeEnum(val code: ErrorTrackerCode) {
    SAVE_FAILED(ErrorTrackerCode("01FK69JRHFKTN1BYZW6BRHFZFJ")),
    FIND_BY_FIELD_FAILED(ErrorTrackerCode("01FK69KJ5JKTN1BYZW6BRHFZFJ")),
    FIND_ALL_BY_FIELD_FAILED(ErrorTrackerCode("01FK69KJ5JKTN1BYZW6BRHFZFJ")),
    FIND_ALL_BY_CRITERIA_FAILED(ErrorTrackerCode("01FMMA8XX09FE3F55670TQCPE3")),
    CAUGHT_EXCEPTION(ErrorTrackerCode("01FK69M8XQKTN1BYZW6BRHFZFJ")),
}
