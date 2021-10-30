package br.com.acmattos.hdc.scheduler.config

import br.com.acmattos.hdc.common.tool.server.javalin.ErrorTrackerCode

/**
 * @author ACMattos
 * @since 27/10/2021.
 */
enum class ErrorTrackerCodeEnum(val code: ErrorTrackerCode) {
    SCHEDULE_ALREADY_DEFINED(ErrorTrackerCode("01FJZAH7JVKTN1BYZW6BRHFZFJ")),
    FROM_BIGGER_THAN_TO(ErrorTrackerCode("01FJZAHV5HKTN1BYZW6BRHFZFJ")),
    OPERATION_GENERATES_REMINDER(ErrorTrackerCode("01FJZAK70FKTN1BYZW6BRHFZFJ")),
    SLOT_BELLOW_ONE(ErrorTrackerCode("01FJZAJFMGKTN1BYZW6BRHFZFJ")),
    FROM_LESS_THAN_TO(ErrorTrackerCode("01FJZAHV5HKTN1BYZW6BRHFZFJ")),
    VALID_SLOT(ErrorTrackerCode("01FJZAJFMGKTN1BYZW6BRHFZFJ")),
    DURATION_CREATES_PERFECT_SLOTS(ErrorTrackerCode("01FJZAK70FKTN1BYZW6BRHFZFJ")),
    WEEK_DAY_CONVERT_FAILED(ErrorTrackerCode("01FJZEYS51KTN1BYZW6BRHFZFJ"))
}
