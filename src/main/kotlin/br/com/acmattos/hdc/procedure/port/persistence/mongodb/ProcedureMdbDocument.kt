package br.com.acmattos.hdc.procedure.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.port.persistence.mongodb.MdbDocument
import br.com.acmattos.hdc.procedure.domain.model.Procedure
import br.com.acmattos.hdc.procedure.domain.model.ProcedureId
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 20/03/2022.
 */
data class ProcedureMdbDocument(
    val procedureId: String,
    val code: Int,
    val description:String,
    val enabled: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
): MdbDocument() {
    constructor(
        procedure: Procedure
    ): this(
        procedureId = procedure.procedureId.id,
        code = procedure.code,
        description = procedure.description,
        enabled = procedure.enabled,
        createdAt = procedure.createdAt,
        updatedAt = procedure.updatedAt
    )

    override fun toType(): Procedure =
        Procedure(
            procedureIdData = ProcedureId(procedureId),
            codeData = code,
            descriptionData = description,
            enabledData = enabled,
            createdAtData = createdAt,
            updatedAtData = updatedAt
        )
}
