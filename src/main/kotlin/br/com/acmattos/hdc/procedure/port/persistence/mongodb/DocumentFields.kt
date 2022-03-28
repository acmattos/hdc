package br.com.acmattos.hdc.procedure.port.persistence.mongodb

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
enum class DocumentIndexedField(val fieldName: String) {
    EVENT_PROCEDURE_ID_ID("event.procedure_id.id"),
    EVENT_CODE("event.code"),
    CODE("code"),
    PROCEDURE_ID("procedure_id"),
    QUERY_ID_ID("query.id.id"),
    QUERY_AUDIT_LOG_WHO("query.audit_log.who"),
}