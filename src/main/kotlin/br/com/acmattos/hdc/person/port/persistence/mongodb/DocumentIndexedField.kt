package br.com.acmattos.hdc.person.port.persistence.mongodb

/**
 * @author ACMattos
 * @since 23/05/2022.
 */
enum class DocumentIndexedField(val fieldName: String) {
    EVENT_PERSON_ID_ID("event.person_id.id"),
    CONTACT("contact"),
    CPF("cpf"),
    FULL_NAME("full_name"),
    PERSON_ID("person_id"),
    CONTACTS_INFO("contacts.info"),
    DENTAL_PLAN("dental_plan"),
    DENTAL_PLAN_NAME("dental_plan.name"),
    QUERY_ID_ID("query.id.id"),
    QUERY_AUDIT_LOG_WHO("query.audit_log.who"),
}