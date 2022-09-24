package br.com.acmattos.hdc.odontogram.domain.cds

import br.com.acmattos.hdc.common.context.domain.cqs.Event
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.odontogram.domain.model.MetaOdontogramId
import br.com.acmattos.hdc.odontogram.domain.model.MetaTooth
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 29/08/2022.
 */
open class MetaOdontogramEvent(
    open val metaOdontogramId: MetaOdontogramId,
    override val auditLog: AuditLog
): Event(auditLog)

/**
 * @author ACMattos
 * @since 29/08/2022.
 */
@JsonTypeName("CreateMetaOdontogramEvent")
data class CreateMetaOdontogramEvent(
    override val metaOdontogramId: MetaOdontogramId,
    val upperLeft: MutableList<MetaTooth>,
    val upperRight: MutableList<MetaTooth>,
    val lowerLeft: MutableList<MetaTooth>,
    val lowerRight: MutableList<MetaTooth>,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): MetaOdontogramEvent(metaOdontogramId, auditLog) {
    constructor(
        command: CreateMetaOdontogramCommand
    ): this(
        metaOdontogramId = command.metaOdontogramId,
        upperLeft =  command.upperLeft,
        upperRight =  command.upperRight,
        lowerLeft =  command.lowerLeft,
        lowerRight =  command.lowerRight,
        enabled = command.enabled,
        createdAt = command.createdAt,
        auditLog = command.auditLog
    )
}
