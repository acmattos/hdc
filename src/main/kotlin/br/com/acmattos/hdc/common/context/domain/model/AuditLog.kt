package br.com.acmattos.hdc.common.context.domain.model

import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 28/06/2020.
 */
data class AuditLog(
    val who: String,
    val what: String,
    val `when`: LocalDateTime = LocalDateTime.now()
): ValueObject