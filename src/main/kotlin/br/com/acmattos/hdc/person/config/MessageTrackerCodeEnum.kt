package br.com.acmattos.hdc.person.config

import br.com.acmattos.hdc.common.tool.server.javalin.MessageTrackerCode

/**
 * @author ACMattos
 * @since 27/10/2021.
 */
enum class MessageTrackerCodeEnum(val code: MessageTrackerCode) {
    PERSON_ALREADY_EXISTS(MessageTrackerCode("01FJZAFP3DKTN1BYZW6BRHFZFJ")),
    PERSON_NOT_DEFINED(MessageTrackerCode("01FWKTXVD4Q2MJX8PESM4KVKSP")),
    PERSON_TYPE_CONVERT_FAILED(MessageTrackerCode("01FJZAF4M4KTN1BYZW6BRHFZFJ")),
    STATUS_CONVERT_FAILED(MessageTrackerCode("01FVT3QG3R57RMXBZDKHVT7ZGJ")),
    STATE_CONVERT_FAILED(MessageTrackerCode("01FJZAGRRNKTN1BYZW6BRHFZFJ")),
    CONTACT_TYPE_CONVERT_FAILED(MessageTrackerCode("01FVQ2NP5J19FBHY10QF033G7Y")),
    MARITAL_STATUS_CONVERT_FAILED(MessageTrackerCode("01FVT3QG3SKKHVWHP57PKD8N62")),
    DENTIST_ID_INVALID(MessageTrackerCode("01FKKM1GEHKTN1BYZW6BRHFZFJ")),
    INVALID_PERSON_FULL_NAME(MessageTrackerCode("01FV2HXNN69FE3F55670TQCPE3")),
    INVALID_PERSON_PERSON_TYPE_DENTIST(MessageTrackerCode("01FVT5ENFKQ2MJX8PESM4KVKSP")),
    INVALID_PERSON_PERSON_TYPE_PATIENT(MessageTrackerCode("01FVT5ENFN9FE3F55670TQCPE3")),
    INVALID_PERSON_OCCUPATION(MessageTrackerCode("01FVT5ENFP3PG1JPAG867MJ6XZ")),
    INVALID_PERSON_INDICATED_BY(MessageTrackerCode("01FWKSM5K552MV85FW4ZTYTE6F")),
    INVALID_PERSON_ADDRESSES(MessageTrackerCode("01FVT5ENFQ19FBHY10QF033G7Y")),
    INVALID_PERSON_CONTACTS(MessageTrackerCode("01FVT5ENFQGRN5Q8KG39FK090X")),
    INVALID_PERSON_CPF(MessageTrackerCode("01FVPVJN7G27PHS1MQ52NS823F")),
    INVALID_PERSON_DOB(MessageTrackerCode("01FWKSM5K19FE3F55670TQCPE3")),
    INVALID_PERSON_PERSONAL_ID(MessageTrackerCode("01FVPVJN7H57RMXBZDKHVT7ZGJ")),
    INVALID_PERSON_STATUS(MessageTrackerCode("01FWKFTFMM57RMXBZDKHVT7ZGJ")),
    INVALID_PERSON_ADDRESS_STREET(MessageTrackerCode("01FVQ2NP5H3PG1JPAG867MJ6XZ")),
    INVALID_PERSON_ADDRESS_NUMBER(MessageTrackerCode("01FWKFTFMG9FE3F55670TQCPE3")),
    INVALID_PERSON_ADDRESS_COMPLEMENT(MessageTrackerCode("01FVQ2NP5KGRN5Q8KG39FK090X")),
    INVALID_PERSON_ADDRESS_ZIP_CODE(MessageTrackerCode("01FVPVJN7D9FE3F55670TQCPE3")),
    INVALID_PERSON_ADDRESS_NEIGHBORHOOD(MessageTrackerCode("01FVPVJN7FGRN5Q8KG39FK090X")),
    INVALID_PERSON_ADDRESS_CITY(MessageTrackerCode("01FVPVJN7F2XQ2K4549SYH51FN")),
    INVALID_PERSON_CONTACT_INFO(MessageTrackerCode("01FVT5ENFTKKHVWHP57PKD8N62")),
    INVALID_PERSON_CONTACT_OBS(MessageTrackerCode("01FWKSM5K657RMXBZDKHVT7ZGJ")),
    INVALID_PERSON_DENTAL_PLAN_NAME(MessageTrackerCode("01FVPVJN7HKKHVWHP57PKD8N62")),
    INVALID_PERSON_DENTAL_PLAN_NUMBER(MessageTrackerCode("01FVQ2NP5FQ2MJX8PESM4KVKSP")),
}
