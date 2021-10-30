package br.com.acmattos.hdc.person.config

import br.com.acmattos.hdc.common.tool.server.javalin.ErrorTrackerCode

/**
 * @author ACMattos
 * @since 27/10/2021.
 */
enum class ErrorTrackerCodeEnum(val code: ErrorTrackerCode) {
    DENTIST_ALREADY_EXISTS(ErrorTrackerCode("01FJZAFP3DKTN1BYZW6BRHFZFJ")),
    PERSON_TYPE_CONVERT_FAILED(ErrorTrackerCode("01FJZAF4M4KTN1BYZW6BRHFZFJ")),
    STATE_CONVERT_FAILED(ErrorTrackerCode("01FJZAGRRNKTN1BYZW6BRHFZFJ")),
}
