package br.com.acmattos.hdc.person.config

import br.com.acmattos.hdc.common.tool.server.javalin.MessageTrackerCode

/**
 * @author ACMattos
 * @since 27/10/2021.
 */
enum class MessageTrackerCodeEnum(val code: MessageTrackerCode) {
    DENTIST_ALREADY_EXISTS(MessageTrackerCode("01FJZAFP3DKTN1BYZW6BRHFZFJ")),
    PERSON_TYPE_CONVERT_FAILED(MessageTrackerCode("01FJZAF4M4KTN1BYZW6BRHFZFJ")),
    STATE_CONVERT_FAILED(MessageTrackerCode("01FJZAGRRNKTN1BYZW6BRHFZFJ")),
    CONTACT_TYPE_CONVERT_FAILED(MessageTrackerCode("01FVQ2NP5J19FBHY10QF033G7Y")),
    DENTIST_ID_INVALID(MessageTrackerCode("01FKKM1GEHKTN1BYZW6BRHFZFJ")),
    INVALID_PERSON_FULL_NAME(MessageTrackerCode("01FM8D9F8C9FE3F55670TQCPE3")),
}
