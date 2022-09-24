package br.com.acmattos.hdc.odontogram.domain.cds

import br.com.acmattos.hdc.common.context.domain.cqs.Command
import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.odontogram.domain.model.MetaOdontogramId
import br.com.acmattos.hdc.odontogram.domain.model.MetaTooth
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 29/08/2022.
 */
open class MetaOdontogramCommand(
    open val metaOdontogramId: MetaOdontogramId,
    override val auditLog: AuditLog
): Command(auditLog)

/**
 * @author ACMattos
 * @since 29/08/2022.
 */
data class CreateMetaOdontogramCommand(
    override val metaOdontogramId: MetaOdontogramId,
    val upperLeft: MutableList<MetaTooth>,
    val upperRight: MutableList<MetaTooth>,
    val lowerLeft: MutableList<MetaTooth>,
    val lowerRight: MutableList<MetaTooth>,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    override val auditLog: AuditLog
): MetaOdontogramCommand(metaOdontogramId, auditLog) {
    constructor(
        upperLeft: MutableList<MetaTooth>,
        upperRight: MutableList<MetaTooth>,
        lowerLeft: MutableList<MetaTooth>,
        lowerRight: MutableList<MetaTooth>,
        auditLog: AuditLog
    ): this(
        MetaOdontogramId(),
        upperLeft,
        upperRight,
        lowerLeft,
        lowerRight,
        true,
        LocalDateTime.now(),
        auditLog
    )
}