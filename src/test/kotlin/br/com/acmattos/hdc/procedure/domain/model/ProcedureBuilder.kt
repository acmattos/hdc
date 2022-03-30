package br.com.acmattos.hdc.procedure.domain.model

import br.com.acmattos.hdc.common.context.domain.model.AuditLog
import br.com.acmattos.hdc.procedure.domain.cqs.CreateDentalProcedureCommand
import br.com.acmattos.hdc.procedure.domain.cqs.CreateDentalProcedureEvent
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 29/03/2022.
 */
class ProcedureBuilder {
    companion object {
        fun build(
            code: Int = 81000014,
            description: String = "Procedure description"
        ) = Procedure.apply(
            CreateDentalProcedureEvent(
                CreateDentalProcedureCommand(
                    ProcedureId("01FK96GENJKTN1BYZW6BRHFZFJ"),
                    code,
                    description,
                    true,
                    LocalDateTime.of(2022, 3, 30, 11, 2, 0),
                    AuditLog("who", "what")
                )
            )
        )

        fun buildWithInvalidCode(code: Int = 1) = build(code = code)

        fun buildWithInvalidDescription(description: String = "I") =
            build(description = description)
    }
}